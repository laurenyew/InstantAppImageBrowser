package laurenyew.imagebrowser.browser.presenters

import android.content.Context
import android.util.Log
import laurenyew.imagebrowser.base.commands.GetRecentImagesCommand
import laurenyew.imagebrowser.base.commands.SearchImagesCommand
import laurenyew.imagebrowser.base.model.ImageData
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.adapters.data.ImagePreviewDataWrapper
import laurenyew.imagebrowser.browser.contracts.ImageBrowserContract
import java.lang.ref.WeakReference

/**
 * Image Browser business logic
 *
 * If no search term is available, shows the recent images. Otherwise, does
 * a search for images matching the given search term
 */
open class ImageBrowserPresenter : ImageBrowserContract.Presenter {
    companion object {
        const val DEFAULT_NUM_IMAGES_PER_PAGE = 30
    }

    private var viewRef: WeakReference<ImageBrowserContract.View>? = null
    private var apiKey: String? = null
    //Only one command should be run at once
    private var searchImagesCommand: SearchImagesCommand? = null
    private var getRecentImagesCommand: GetRecentImagesCommand? = null
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
        searchImagesCommand?.cancel()
        searchImagesCommand = null
        data.clear()
        searchTerm = null
    }

    /**
     * Refresh the list of images for the given search term starting at the first page
     */
    override fun refreshImages(searchTerm: String) {
        //Only let 1 command run at once
        getRecentImagesCommand?.cancel()
        searchImagesCommand?.cancel()

        //reset the current page
        currentPageNum = 1
        this.searchTerm = searchTerm

        //load the images async
        val currentApiKey = apiKey
        if (currentApiKey != null) {
            if (searchTerm.isNotEmpty()) {
                searchImagesCommand = SearchImagesCommand(currentApiKey, searchTerm, numImagesPerPage, currentPageNum,
                        { list, pageNum, totalNumPages -> onLoadImagesSuccess(list, pageNum, totalNumPages) },
                        { errorCode -> onLoadImagesFailure(errorCode) })
                searchImagesCommand?.execute()
            } else {
                getRecentImagesCommand = GetRecentImagesCommand(currentApiKey, numImagesPerPage, currentPageNum,
                        { list, pageNum, totalNumPages -> onLoadImagesSuccess(list, pageNum, totalNumPages) },
                        { errorCode -> onLoadImagesFailure(errorCode) })
                getRecentImagesCommand?.execute()
            }
        }
    }

    /**
     * If we have more pages available to load, load the next page of images for the
     * given search term
     */
    override fun loadNextPageOfImages() {
        //Only let 1 command run at once
        getRecentImagesCommand?.cancel()
        searchImagesCommand?.cancel()

        val nextPageNum = currentPageNum + 1
        val currentApiKey = apiKey
        if (currentApiKey != null && nextPageNum <= totalNumPages) {
            //load the images async for next page
            if (searchTerm?.isNotEmpty() == true) {
                searchImagesCommand = SearchImagesCommand(currentApiKey, searchTerm
                        ?: "", numImagesPerPage, nextPageNum,
                        { list, pageNum, totalNumPages -> onLoadImagesSuccess(list, pageNum, totalNumPages) },
                        { errorCode -> onLoadImagesFailure(errorCode) })
                searchImagesCommand?.execute()
            } else {
                getRecentImagesCommand = GetRecentImagesCommand(currentApiKey, numImagesPerPage, currentPageNum,
                        { list, pageNum, totalNumPages -> onLoadImagesSuccess(list, pageNum, totalNumPages) },
                        { errorCode -> onLoadImagesFailure(errorCode) })
                getRecentImagesCommand?.execute()
            }
        }
    }

    override fun onSelectPreview(itemId: String, itemImageUrl: String, itemTitle: String?) {
        view?.onShowImageDetail(itemId, itemImageUrl, itemTitle)
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
        images.forEach {
            data.add(ImagePreviewDataWrapper(it.id, it.imageUrl, it.title))
        }

        //Update the view
        view?.onImagesLoaded(data)
    }

    /**
     * Update the view about the load failure
     */
    open fun onLoadImagesFailure(error: String?) {
        view?.onImagesLoaded(null)
        view?.onImagesFailedToLoad()
        Log.d("ImageBrowserPresenter", "Load Images Failed. Error: $error")
    }
    //endregion
}