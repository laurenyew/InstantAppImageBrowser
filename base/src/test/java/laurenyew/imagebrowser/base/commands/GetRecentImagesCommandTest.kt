@file:Suppress("IllegalIdentifier")

package laurenyew.imagebrowser.base.commands

import com.nhaarman.mockito_kotlin.*
import laurenyew.imagebrowser.base.BuildConfig
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class GetRecentImagesCommandTest : FlickrImageCommandTestBase() {
    private lateinit var command: GetRecentImagesCommand

    override fun setup() {
        super.setup()
        command = spy(GetRecentImagesCommand(apiKey, numImagesPerPage, pageNum, mockOnSuccess, mockOnFailure))
        command.flickrImageApi = mockFlickrApi
    }

    @Test
    fun `given command run, api should get request for images`() {
        /** Exercise **/
        command.executeCommandImpl()

        /** Verify **/
        verify(mockFlickrApi).getRecentPhotos(any(), any(), any(), anyOrNull())
    }

    @Test
    fun `given command run and api returns success, get request should hit onSuccess with results`() {
        /** Arrange **/
        whenever(mockCallResponse.execute()).thenReturn(successResponse)

        /** Exercise **/
        command.executeCommandImpl()

        /** Verify **/
        //OnSuccess should have been called
        assertNull(resultError)
        assertNotNull(resultData)
        assertEquals(numImagesPerPage, resultData!!.size)
        val expectedUrl1 = getExpectedUrl(flickrImage1.id, flickrImage1.secret, flickrImage1.farm, flickrImage1.server)
        resultData!![0].apply {
            assertEquals(flickrImage1.id, id)
            assertEquals(flickrImage1.title, title)
            assertEquals(expectedUrl1, imageUrl)
        }
        val expectedUrl2 = getExpectedUrl(flickrImage2.id, flickrImage2.secret, flickrImage2.farm, flickrImage2.server)
        resultData!![1].apply {
            assertEquals(flickrImage2.id, id)
            assertEquals(flickrImage2.title, title)
            assertEquals(expectedUrl2, imageUrl)
        }
        val expectedUrl3 = getExpectedUrl(flickrImage3.id, flickrImage3.secret, flickrImage3.farm, flickrImage3.server)
        resultData!![2].apply {
            assertEquals(flickrImage3.id, id)
            assertEquals(flickrImage3.title, title)
            assertEquals(expectedUrl3, imageUrl)
        }
    }

    @Test
    fun `given command run and api returns failure, get request should hit onSuccess with results`() {
        /** Arrange **/
        whenever(mockCallResponse.execute()).thenReturn(failureResponse)

        /** Exercise **/
        command.executeCommandImpl()

        /** Verify **/
        //OnSuccess should have been called
        assertNull(resultData)
        assertNotNull(resultError)
        assertEquals(expectedErrorCode.toString(), resultError)
    }
}