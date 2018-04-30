package laurenyew.imagebrowser.browser.adapters

import android.os.Handler
import android.support.annotation.VisibleForTesting
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.adapters.data.ImageDiffCallback
import laurenyew.imagebrowser.browser.adapters.data.ImagePreviewDataWrapper
import laurenyew.imagebrowser.browser.adapters.viewHolder.ImagePreviewViewHolder
import laurenyew.imagebrowser.browser.contracts.ImageBrowserContract
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * ImageBrowser Preview RecyclerView Adapter
 * Uses DiffUtil for better performance on adapter updates
 */
open class ImageBrowserRecyclerViewAdapter(private val presenter: ImageBrowserContract.Presenter?) : RecyclerView.Adapter<ImagePreviewViewHolder>() {
    private var data: MutableList<ImagePreviewDataWrapper> = ArrayList()
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var pendingDataUpdates = ArrayDeque<List<ImagePreviewDataWrapper>>()

    //RecyclerView Diff.Util (List Updates)
    open fun updateData(newData: List<ImagePreviewDataWrapper>?) {
        val data = newData ?: ArrayList()
        pendingDataUpdates.add(data)
        if (pendingDataUpdates.size <= 1) {
            updateDataInternal(data)
        }
    }

    /**
     * Handle the diff util update on a background thread
     * (this can take O(n) time so we don't want it on the main thread)
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    open fun updateDataInternal(newData: List<ImagePreviewDataWrapper>?) {
        val oldData = ArrayList(data)

        val handler = Handler()
        Thread(Runnable {
            val diffCallback = createDataDiffCallback(oldData, newData)
            val diffResult = DiffUtil.calculateDiff(diffCallback)

            handler.post {
                applyDataDiffResult(newData, diffResult)
            }
        }).start()
    }

    /**
     * UI thread callback to apply the diff result to the adapter
     * and take in the latest update
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    open fun applyDataDiffResult(newData: List<ImagePreviewDataWrapper>?, diffResult: DiffUtil.DiffResult) {
        if (pendingDataUpdates.isNotEmpty()) {
            pendingDataUpdates.remove()
        }

        //Apply the data to the view
        data.clear()
        if (newData != null) {
            data.addAll(newData)
        }
        diffResult.dispatchUpdatesTo(this)

        //Take in the next latest update
        if (pendingDataUpdates.isNotEmpty()) {
            val latestDataUpdate = pendingDataUpdates.pop()
            pendingDataUpdates.clear()
            updateDataInternal(latestDataUpdate)
        }
    }

    open fun createDataDiffCallback(oldData: List<ImagePreviewDataWrapper>?, newData: List<ImagePreviewDataWrapper>?): DiffUtil.Callback =
            ImageDiffCallback(oldData, newData)
    //endregion

    //region RecyclerView.Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePreviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_browser_preview_view, parent, false)
        return ImagePreviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagePreviewViewHolder, position: Int) {
        val item = data[position]
        val itemImageUrl = item.imageUrl
        if (holder.imageView != null) {
            Picasso.get()
                    .load(itemImageUrl)
                    .placeholder(laurenyew.imagebrowser.base.R.drawable.image_placeholder)
                    .error(laurenyew.imagebrowser.base.R.drawable.image_placeholder)
                    .into(holder.imageView)
        }
        holder.view.setOnClickListener {
            presenter?.onSelectPreview(item.id, item.imageUrl, item.imageTitle)
        }
    }

    override fun getItemCount(): Int = data.size
    //endregion
}