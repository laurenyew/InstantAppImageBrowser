package laurenyew.imagebrowser.browser.contracts

import android.content.Context
import laurenyew.imagebrowser.browser.adapters.data.ImagePreviewDataWrapper

interface ImageBrowserContract {
    interface View {
        fun onImagesLoaded(data: List<ImagePreviewDataWrapper>?)
        fun onImagesFailedToLoad()
        fun onShowImageDetail(itemId: String, itemImageUrl: String, itemTitle: String?)
    }

    interface Presenter {
        /**
         * Available to be overwritten if we want to change the number of images per page
         */
        val numImagesPerPage: Int

        fun onBind(view: View, context: Context?)
        fun unBind()
        /**
         * Each new search term will refresh the images loaded
         */
        fun refreshImages(searchTerm: String)

        /**
         * Will add images for a given pageNum to the current list
         */
        fun loadNextPageOfImages()

        fun onSelectPreview(itemId: String, itemImageUrl: String, itemTitle: String?)
    }
}