package laurenyew.imagebrowser.base.commands

import android.os.Handler
import android.os.Looper
import android.support.annotation.VisibleForTesting
import android.util.Log
import laurenyew.imagebrowser.base.api.FlickrImageApi
import laurenyew.imagebrowser.base.model.ImageData
import laurenyew.imagebrowser.base.networking.FlickrApiBuilder

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * Command used by the browser feature to search images.
 *
 * NOTE: As per Flickr Api, search term should not be empty. Use the [GetRecentImagesCommand] instead.
 * Execute is provided so we can run this command whenever we'd like
 *
 * This command takes care of executing in the background, and updates the listener
 * functions passed to it on the UI thread.
 *
 * On Success function returns the image data list, the page num, and the total number of pages
 * Failure function is optional
 */
open class SearchImagesCommand(private val apiKey: String, private val searchTerm: String,
                               private val numImagesPerPage: Int,
                               private val pageNum: Int,
                               private val onSuccess: (List<ImageData>, Int, Int) -> Unit,
                               private val onFailure: ((String?) -> Unit)? = null) : AsyncJobCommand() {
    private var _flickrApi = FlickrApiBuilder.apiBuilder(FlickrImageApi::class.java)
    var flickrImageApi: FlickrImageApi?
        get() = _flickrApi
        @VisibleForTesting
        set(api) {
            _flickrApi = api
        }

    override fun executeCommandImpl() {
        val handler = Handler(Looper.getMainLooper())
        val searchImagesApi = flickrImageApi
        val extraArgs = HashMap<String, String>()
        extraArgs["privacy_filter"] = "1"
        extraArgs["safe_search"] = "2" //Moderate Safe search
        extraArgs["content_type"] = "1" //Photos only
        extraArgs["sort"] = "relevance"

        try {
            val photosCall = searchImagesApi?.searchPhotos(apiKey, searchTerm, numImagesPerPage, pageNum, extraArgs)
            val photosResponse = photosCall?.execute()
            val photoPageData = photosResponse?.body()?.pageResponse
            if (photosResponse?.code() != 200 || photoPageData == null) {
                //Post failure onto main thread
                handler.post {
                    onFailure?.invoke(photosResponse?.code()?.toString())
                }
            } else {
                val images = ArrayList<ImageData>()
                photoPageData.photos.forEach {
                    val imageUrl = "https://farm${it.farm}.staticflickr.com/${it.server}/${it.id}_${it.secret}.jpg"
                    images.add(ImageData(it.id, it.title, imageUrl))
                }
                handler.post {
                    onSuccess(images, photoPageData.currentPageNum, photoPageData.totalNumPages)
                }
            }
        } catch (e: Exception) {
            Log.d("SearchImagesCommand", "Failed to get photos for searchTerm: $searchTerm", e)
            handler.post {
                onFailure?.invoke(e.localizedMessage)
            }
        }
    }
}