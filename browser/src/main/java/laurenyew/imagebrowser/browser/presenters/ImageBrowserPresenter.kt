package laurenyew.imagebrowser.browser.presenters

import android.content.Context
import android.support.annotation.VisibleForTesting
import android.util.Log
import laurenyew.imagebrowser.base.SharedPrefConfig
import laurenyew.imagebrowser.base.commands.AsyncJobCommand
import laurenyew.imagebrowser.base.commands.GetRecentImagesCommand
import laurenyew.imagebrowser.base.commands.SearchImagesCommand
import laurenyew.imagebrowser.base.model.ImageData
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.adapters.data.ImagePreviewDataWrapper
import laurenyew.imagebrowser.browser.contracts.ImageBrowserContract
import java.lang.ref.WeakReference

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * Image Browser business logic
 *
 * If no search term is available, shows the recent images. Otherwise, does
 * a search for images matching the given search term.
 * Also has logic for defining a default search term.
 */
open class ImageBrowserPresenter : ImageBrowserContract.Presenter {
    companion object {
        const val DEFAULT_NUM_IMAGES_PER_PAGE = 30
    }

    private var viewRef: WeakReference<ImageBrowserContract.View>? = null
    private var data: ArrayList<ImagePreviewDataWrapper> = ArrayList()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var apiKey: String? = null

    //Only one command should be run at once
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var command: AsyncJobCommand? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var currentPageNum: Int = 1

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var searchTerm: String? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var totalNumPages: Int = 100

    private var defaultSearchTerm: String = ""

    //region Getters
    val view: ImageBrowserContract.View?
        get() = viewRef?.get()
    //endregion

    //region MVP
    override val numImagesPerPage: Int
        get() = DEFAULT_NUM_IMAGES_PER_PAGE

    override fun onBind(view: ImageBrowserContract.View, context: Context?) {
        viewRef = WeakReference(view)
        apiKey = context?.getString(laurenyew.imagebrowser.base.R.string.flickr_api_key)
        defaultSearchTerm = getDefaultSearchTerm(context)
    }

    override fun unBind() {
        viewRef?.clear()
        viewRef = null
        command?.cancel()
        command = null
        data.clear()
        searchTerm = null
        apiKey = null
    }

    /**
     * Refresh the list of images for the given search term starting at the first page
     */
    override fun refreshImages(query: String?) {
        //Only let 1 command run at once
        command?.cancel()

        //reset the current page
        val searchQuery = if (query != null && query.isNotEmpty()) query else defaultSearchTerm
        searchTerm = searchQuery
        currentPageNum = 1

        //load the images async
        val currentApiKey = apiKey
        if (currentApiKey != null) {
            command = if (searchQuery.isNotEmpty()) {
                SearchImagesCommand(currentApiKey, searchQuery, numImagesPerPage, currentPageNum,
                        { list, pageNum, totalNumPages -> onLoadImagesSuccess(list, pageNum, totalNumPages) },
                        { errorCode -> onLoadImagesFailure(errorCode) })

            } else {
                GetRecentImagesCommand(currentApiKey, numImagesPerPage, currentPageNum,
                        { list, pageNum, totalNumPages -> onLoadImagesSuccess(list, pageNum, totalNumPages) },
                        { errorCode -> onLoadImagesFailure(errorCode) })
            }
            command?.execute()
        }
    }

    /**
     * If we have more pages available to load, load the next page of images for the
     * given search term
     */
    override fun loadNextPageOfImages() {
        //Only let 1 command run at once
        command?.cancel()

        val nextPageNum = currentPageNum + 1
        val currentApiKey = apiKey
        if (currentApiKey != null && nextPageNum <= totalNumPages) {
            //load the images async for next page
            command = if (searchTerm?.isNotEmpty() == true) {
                SearchImagesCommand(currentApiKey, searchTerm
                        ?: "", numImagesPerPage, nextPageNum,
                        { list, pageNum, totalNumPages -> onLoadImagesSuccess(list, pageNum, totalNumPages) },
                        { errorCode -> onLoadImagesFailure(errorCode) })

            } else {
                GetRecentImagesCommand(currentApiKey, numImagesPerPage, currentPageNum,
                        { list, pageNum, totalNumPages -> onLoadImagesSuccess(list, pageNum, totalNumPages) },
                        { errorCode -> onLoadImagesFailure(errorCode) })

            }
            command?.execute()
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
        Log.d("ImageBrowserPresenter", "Load Images Failed. Error: $error")
        view?.onImagesFailedToLoad()
    }

    /**
     * Helper method for default search term.
     * Includes logic for debug feature: show recent items
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    open fun getDefaultSearchTerm(context: Context?): String {
        if (context != null) {
            val sharedPrefs = context.getSharedPreferences(SharedPrefConfig.BROWSER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val shouldShowRecentItems = sharedPrefs.getBoolean(SharedPrefConfig.SHOULD_SHOW_RECENT_IMAGES, false)
            if (!shouldShowRecentItems) {
                return context.getString(R.string.image_browser_base_search_term) ?: ""
            }
        }
        return ""
    }
    //endregion
}