package laurenyew.imagebrowser.browser.adapters.data

import android.support.v7.util.DiffUtil

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * DiffUtil.Callback that compares image preview data wrappers
 * (Used in [laurenyew.imagebrowser.browser.adapters.ImageBrowserRecyclerViewAdapter]
 */
open class ImageDiffCallback(private val oldData: List<ImagePreviewDataWrapper>?, private val newData: List<ImagePreviewDataWrapper>?) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldData?.size ?: 0

    override fun getNewListSize(): Int = newData?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData?.get(oldItemPosition)
        val newItem = newData?.get(newItemPosition)

        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData?.get(oldItemPosition)
        val newItem = newData?.get(newItemPosition)

        return oldItem?.imageUrl == newItem?.imageUrl && oldItem?.imageTitle == newItem?.imageTitle
    }
}