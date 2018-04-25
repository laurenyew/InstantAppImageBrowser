package laurenyew.imagebrowser.browser.adapters.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import laurenyew.imagebrowser.browser.R

open class ImagePreviewViewHolder(val view: View): RecyclerView.ViewHolder(view){
    val imageView = view.findViewById<ImageView>(R.id.previewImageView)
}