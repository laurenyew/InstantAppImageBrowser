package laurenyew.imagebrowser.base.commands

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import laurenyew.imagebrowser.base.api.FlickrImageApi
import laurenyew.imagebrowser.base.api.response.FlickrImageDataResponse
import laurenyew.imagebrowser.base.api.response.FlickrPhotoPageResponse
import laurenyew.imagebrowser.base.api.response.FlickrPhotosResponse
import laurenyew.imagebrowser.base.model.ImageData
import okhttp3.ResponseBody
import org.junit.Before
import retrofit2.Call
import retrofit2.Response

/**
 * Base Test class for FlickrImageCommands
 */
open class FlickrImageCommandTestBase {
    val mockFlickrApi: FlickrImageApi = mock()
    val mockCallResponse: Call<FlickrPhotosResponse?> = mock()
    val mockOnSuccess: (List<ImageData>, Int, Int) -> Unit = { data, pageNum, numPages -> mockOnSuccess(data, pageNum, numPages) }
    val mockOnFailure: (String?) -> Unit = { error -> mockOnFailure(error) }

    val apiKey = "test_key"
    val numImagesPerPage = 3
    var pageNum = 1

    val expectedErrorCode = 404

    val flickrImage1 = FlickrImageDataResponse("id1", "owner1", "secret1", "server1", "farm1", "title1")
    val flickrImage2 = FlickrImageDataResponse("id2", "owner2", "secret2", "server2", "farm2", "title2")
    val flickrImage3 = FlickrImageDataResponse("id3", "owner3", "secret3", "server3", "farm3", "title3")

    private val flickrImageList = arrayListOf(flickrImage1, flickrImage2, flickrImage3)
    private val flickrPage = FlickrPhotoPageResponse(pageNum, pageNum, numImagesPerPage, numImagesPerPage.toString(), flickrImageList)

    private val flickrSuccessResponse: FlickrPhotosResponse = FlickrPhotosResponse(flickrPage)
    val successResponse: Response<FlickrPhotosResponse?> = Response.success(flickrSuccessResponse)
    val failureResponse: Response<FlickrPhotosResponse?> = Response.error(expectedErrorCode, ResponseBody.create(null, "Error"))

    var resultData: List<ImageData>? = null
    var resultPageNum: Int? = null
    var resultNumPages: Int? = null
    var resultError: String? = null


    @Before
    open fun setup() {
        whenever(mockFlickrApi.getRecentPhotos(any(), any(), any(), anyOrNull())).thenReturn(mockCallResponse)
        whenever(mockFlickrApi.searchPhotos(any(), any(), any(), any(), anyOrNull())).thenReturn(mockCallResponse)
    }

    //region Helper Methods
    open fun mockOnSuccess(data: List<ImageData>, pageNum: Int, totalPages: Int) {
        resultData = data
        resultPageNum = pageNum
        resultNumPages = totalPages
    }

    open fun mockOnFailure(error: String?) {
        resultError = error
    }

    fun getExpectedUrl(id: String, secret: String, farm: String, server: String): String = "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}.jpg"
    //endregion
}