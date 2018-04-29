package laurenyew.imagebrowser.browser.presenters

import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import laurenyew.imagebrowser.base.BuildConfig
import laurenyew.imagebrowser.base.commands.GetRecentImagesCommand
import laurenyew.imagebrowser.base.commands.SearchImagesCommand
import laurenyew.imagebrowser.base.model.ImageData
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.contracts.ImageBrowserContract
import laurenyew.imagebrowser.browser.helpers.ImageBrowserTestBase
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ImageBrowserPresenterTest : ImageBrowserTestBase() {
    private val presenter = ImageBrowserPresenter()
    private val mockView: ImageBrowserContract.View = mock()

    override fun setup() {
        super.setup()
        presenter.onBind(mockView, context)
    }

    @After
    fun tearDown() {
        presenter.unBind()
    }

    @Test
    fun `when onBind, presenter should set up the view reference and api key`() {
        /** Verify **/
        assertNotNull(presenter.view)
        assertEquals(mockView, presenter.view)
        assertEquals(context.getString(R.string.flickr_api_key), presenter.apiKey)
    }

    @Test
    fun `when unbind, presenter should teardonw view reference and api key`() {
        /** Exercise **/
        presenter.unBind()

        /** Verify **/
        assertNull(presenter.view)
        assertNull(presenter.apiKey)
    }

    @Test
    fun `when refreshImages with valid search term, should create search images command`() {
        /** Exercise **/
        presenter.refreshImages("test")

        /** Verify **/
        assertNotNull(presenter.command)
        assertTrue(presenter.command is SearchImagesCommand)
        assertEquals(1, presenter.currentPageNum)
        assertEquals("test", presenter.searchTerm)
    }

    @Test
    fun `when refreshImages with empty search term, should create get recent images command`() {
        /** Exercise **/
        presenter.refreshImages("")

        /** Verify **/
        assertNotNull(presenter.command)
        assertTrue(presenter.command is GetRecentImagesCommand)
        assertEquals(1, presenter.currentPageNum)
        assertEquals("", presenter.searchTerm)
    }

    @Test
    fun `when loadNextPageOfImages with search term, command is search command`() {
        /** Arrange **/
        presenter.refreshImages("test")

        /** Exercise **/
        presenter.loadNextPageOfImages()

        /** Verify **/
        assertNotNull(presenter.command)
        assertTrue(presenter.command is SearchImagesCommand)
        assertEquals(1, presenter.currentPageNum)// page num not incremented until page comes back
        assertEquals("test", presenter.searchTerm)
    }

    @Test
    fun `when loadNextPageOfImages with empty search term, command is get recent photos command`() {
        /** Arrange **/
        presenter.refreshImages("")

        /** Exercise **/
        presenter.loadNextPageOfImages()

        /** Verify **/
        assertNotNull(presenter.command)
        assertTrue(presenter.command is GetRecentImagesCommand)
        assertEquals(1, presenter.currentPageNum) // page num not incremented until page comes back
        assertEquals("", presenter.searchTerm)
    }

    @Test
    fun `when presenter is requested to select a preview, presenter should update the view`() {
        /** Exercise **/
        presenter.onSelectPreview("id", "url", "title")

        /** Verify **/
        verify(mockView).onShowImageDetail("id", "url", "title")
    }

    @Test
    fun `when onLoadImagesSuccess, current page num is updated, total num pages is updated, and view is updated`() {
        /** Arrange **/
        val data = ArrayList<ImageData>()
        data.add(ImageData("id", "title", "url"))

        /** Exercise **/
        presenter.onLoadImagesSuccess(data, 2, 3)

        /** Verify **/
        assertEquals(2, presenter.currentPageNum)
        assertEquals(3, presenter.totalNumPages)
        verify(mockView).onImagesLoaded(argThat {
            size == 1
                    && get(0).id == "id"
                    && get(0).imageTitle == "title"
                    && get(0).imageUrl == "url"
        })
    }

    @Test
    fun `when onLoadImagesSuccess with null data,view is updated with empty list`() {
        /** Exercise **/
        presenter.onLoadImagesSuccess(arrayListOf(), 1, 3)

        /** Verify **/
        assertEquals(1, presenter.currentPageNum)
        assertEquals(3, presenter.totalNumPages)
        verify(mockView).onImagesLoaded(argThat { size == 0 })
    }

    @Test
    fun `when onLoadImagesSuccess with paged data, view is updated with appended list`() {
        /** Arrange **/
        val data = ArrayList<ImageData>()
        data.add(ImageData("id", "title", "url"))
        val data2 = ArrayList<ImageData>()
        data2.add(ImageData("id2", "title2", "url2"))

        /** Exercise **/
        presenter.onLoadImagesSuccess(data, 1, 3)
        presenter.onLoadImagesSuccess(data2, 2, 3)

        /** Verify **/
        assertEquals(2, presenter.currentPageNum)
        assertEquals(3, presenter.totalNumPages)
        verify(mockView, times(2)).onImagesLoaded(argThat { size == 2 })
    }

    @Test
    fun `when onLoadImagesSuccess with first page, view is updated with refreshed list`() {
        /** Arrange **/
        val data = ArrayList<ImageData>()
        data.add(ImageData("id", "title", "url"))
        val data2 = ArrayList<ImageData>()
        data2.add(ImageData("id2", "title2", "url2"))

        /** Exercise **/
        presenter.onLoadImagesSuccess(data, 1, 3)
        presenter.onLoadImagesSuccess(data2, 2, 3)
        presenter.onLoadImagesSuccess(data, 1, 3)

        /** Verify **/
        assertEquals(1, presenter.currentPageNum)
        assertEquals(3, presenter.totalNumPages)
        verify(mockView).onImagesLoaded(argThat { size == 1 })
    }

    @Test
    fun `when onLoadImagesFailure, data is left intact, view is updated to show failure`() {
        /** Arrange **/
        val data = ArrayList<ImageData>()
        data.add(ImageData("id", "title", "url"))
        presenter.onLoadImagesSuccess(data, 1, 3)

        /** Exercise **/
        presenter.onLoadImagesFailure("Error")

        /** Verify **/
        verify(mockView).onImagesFailedToLoad()
    }

    @Test
    fun `when refresh after loading next page, reset current page num`() {
        /** Arrange **/
        presenter.refreshImages("test")
        presenter.loadNextPageOfImages()
        presenter.onLoadImagesSuccess(arrayListOf(), 2, 2)
        assertEquals(2, presenter.currentPageNum)

        /** Exercise **/
        presenter.refreshImages("test2")

        /** Verify **/
        assertNotNull(presenter.command)
        assertTrue(presenter.command is SearchImagesCommand)
        assertEquals(1, presenter.currentPageNum)
        assertEquals("test2", presenter.searchTerm)
    }

    @Test
    fun `when attempting to load next page and past total num, command won't run`() {
        /** Arrange **/
        presenter.totalNumPages = 2
        presenter.currentPageNum = 2

        /** Exercise **/
        presenter.loadNextPageOfImages()

        /** Verify **/
        assertNull(presenter.command)
    }

    @Test
    fun `when attempting to load next page and past total num but refresh then page, command will run`() {
        /** Arrange **/
        presenter.totalNumPages = 2
        presenter.currentPageNum = 2
        presenter.refreshImages("")

        /** Exercise **/
        presenter.loadNextPageOfImages()

        /** Verify **/
        assertNotNull(presenter.command)
        assertTrue(presenter.command is GetRecentImagesCommand)
        assertEquals(1, presenter.currentPageNum) // page num not incremented until page comes back
        assertEquals("", presenter.searchTerm)
    }
}