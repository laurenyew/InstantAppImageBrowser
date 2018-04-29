package laurenyew.imagebrowser.browser

import laurenyew.imagebrowser.base.BuildConfig
import laurenyew.imagebrowser.browser.contracts.ImageDetailContract
import laurenyew.imagebrowser.browser.fragments.ImageBrowserFragment
import laurenyew.imagebrowser.browser.fragments.ImageDetailFragment
import laurenyew.imagebrowser.browser.presenters.ImageBrowserPresenter
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ImageBrowserFeatureModuleManagerTest {
    @Test
    fun `getImageDetailActivity should have the proper arguments`() {
        /** Arrange **/
        val context = RuntimeEnvironment.application.applicationContext
        val id = "testId"
        val imgUrl = "imgUrl"
        val imgTitle = "imgTitle"

        /** Exercise **/
        val intent = ImageBrowserFeatureModuleManager.getImageDetailActivity(context, id, imgUrl, imgTitle)

        /** Verify **/
        assertNotNull(intent)
        assertEquals(id, intent.getStringExtra(ImageDetailContract.View.ARG_ITEM_ID))
        assertEquals(imgUrl, intent.getStringExtra(ImageDetailContract.View.ARG_ITEM_IMAGE_URL))
        assertEquals(imgTitle, intent.getStringExtra(ImageDetailContract.View.ARG_ITEM_IMAGE_TITLE))
    }

    @Test
    fun `getImageBrowserView should return ImageBrowserFragment`() {
        /** Exercise **/
        val view = ImageBrowserFeatureModuleManager.getImageBrowserView()

        /** Verify **/
        assertNotNull(view)
        assertTrue(view is ImageBrowserFragment)
    }

    @Test
    fun `getImageDetailView should return ImageDetailFragment`() {
        /** Arrange **/
        val id = "testId"
        val imgUrl = "imgUrl"
        val imgTitle = "imgTitle"

        /** Exercise **/
        val view = ImageBrowserFeatureModuleManager.getImageDetailView(id, imgUrl, imgTitle)

        /** Verify **/
        assertNotNull(view)
        assertTrue(view is ImageDetailFragment)
        val fragment = view as ImageDetailFragment
        assertNotNull(fragment.arguments)
        assertEquals(id, fragment.arguments!![ImageDetailContract.View.ARG_ITEM_ID])
        assertEquals(imgUrl, fragment.arguments!![ImageDetailContract.View.ARG_ITEM_IMAGE_URL])
        assertEquals(imgTitle, fragment.arguments!![ImageDetailContract.View.ARG_ITEM_IMAGE_TITLE])
    }

    @Test
    fun `getImageBrowserPresenter should return expected Presenter`() {
        /** Exercise **/
        val presenter = ImageBrowserFeatureModuleManager.getImageBrowserPresenter()

        /** Verify **/
        assertNotNull(presenter)
        assertTrue(presenter is ImageBrowserPresenter)
    }
}