package laurenyew.imagebrowser.browser.presenters

import android.content.Context
import android.util.Log
import laurenyew.imagebrowser.base.commands.GetImagesCommand
import laurenyew.imagebrowser.base.model.ImageData
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.adapters.data.ImagePreviewDataWrapper
import laurenyew.imagebrowser.browser.contracts.ImageBrowserContract
import java.lang.ref.WeakReference

open class ImageBrowserPresenter : ImageBrowserContract.Presenter {
    companion object {
        const val DEFAULT_NUM_IMAGES_PER_PAGE = 30
    }

    private var viewRef: WeakReference<ImageBrowserContract.View>? = null
    private var apiKey: String? = null
    //Only one command can be run at once
    private var command: GetImagesCommand? = null
    private var data: ArrayList<ImagePreviewDataWrapper> = ArrayList()

    private var currentPageNum: Int = 1
    private var searchTerm: String? = null
    private var totalNumPages: Int = 100

    //region Getters
    val view: ImageBrowserContract.View?
        get() = viewRef?.get()
    //endregion

    //region MVP
    override val numImagesPerPage: Int
        get() = DEFAULT_NUM_IMAGES_PER_PAGE

    override fun onBind(view: ImageBrowserContract.View, context: Context?) {
        viewRef = WeakReference(view)
        apiKey = context?.getString(R.string.flickr_api_key)
    }

    override fun unBind() {
        viewRef?.clear()
        viewRef = null
        command?.cancel()
        command = null
        data.clear()
        searchTerm = null
    }

    /**
     * Refresh the list of images for the given search term starting at the first page
     */
    override fun refreshImages(searchTerm: String) {
        command?.cancel()
        //reset the current page
        currentPageNum = 1
        this.searchTerm = searchTerm

        //load the images async
        val currentApiKey = apiKey
        if (currentApiKey != null) {
            command = GetImagesCommand(currentApiKey, searchTerm, numImagesPerPage, currentPageNum,
                    { list, pageNum, totalNumPages -> onLoadImagesSuccess(list, pageNum, totalNumPages) },
                    { errorCode -> onLoadImagesFailure(errorCode) })
            command?.execute()
        }
    }

    /**
     * If we have more pages available to load, load the next page of images for the
     * given search term
     */
    override fun loadNextPageOfImages() {
        val nextPageNum = currentPageNum + 1
        val currentApiKey = apiKey
        if (currentApiKey != null && nextPageNum <= totalNumPages) {
            //load the images async for next page
            command = GetImagesCommand(currentApiKey, searchTerm
                    ?: "", numImagesPerPage, nextPageNum,
                    { list, pageNum, totalNumPages -> onLoadImagesSuccess(list, pageNum, totalNumPages) },
                    { errorCode -> onLoadImagesFailure(errorCode) })
            command?.execute()
        }
    }

    override fun onSelectPreview(itemId: String, itemImageUrl: String) {
        view?.onShowImageDetail(itemId, itemImageUrl)
    }

    //endregion

    //region Helper Methods
    /**
     * If this is the first page, reset the list
     * Otherwise, add to the list
     * Update the view with the new data
     */
    open fun onLoadImagesSuccess(images: List<ImageData>, pageNum: Int, totalNumPages: Int) {
        currentPageNum = pageNum
        this.totalNumPages = totalNumPages
        //As per FlickrAPI, first page is 1
        //So if this is the first page, clear data
        if (pageNum == 1) {
            data = ArrayList()
        }
        //Add all the images
        images?.forEach {
            data.add(ImagePreviewDataWrapper(it.id, it.imageUrl))
        }

        //Update the view
        view?.onImagesLoaded(data)
    }

    /**
     * Update the view about the load failure
     */
    open fun onLoadImagesFailure(errorCode: Int?) {
        view?.onImagesLoaded(null)
        view?.onImagesFailedToLoad()
        Log.d("ImageBrowserPresenter", "Load Images Failed. Error code: $errorCode")
    }
    //endregion
}