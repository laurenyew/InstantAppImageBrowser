package laurenyew.imagebrowser.homepage

import android.content.Context
import android.content.Intent.URI_ALLOW_UNSAFE
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import laurenyew.imagebrowser.base.BuildConfig
import laurenyew.imagebrowser.base.ImageBrowserConfig
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class HomePageFeatureModuleMangerTest {
    private val spyContext: Context = spy(RuntimeEnvironment.application.applicationContext)

    @Test
    fun `given getImageBrowserInstantAppIntent, should return proper intent with expected uri and args`() {
        /** Exercise **/
        val intent = HomePageFeatureModuleManager.getImageBrowserInstantAppIntent(spyContext)

        /** Verify **/
        assertNotNull(intent)
        assertNull(intent.getStringExtra(ImageBrowserConfig.ARG_SEARCH_TERM))
        val uri = intent.toUri(URI_ALLOW_UNSAFE)
        assertNotNull(uri)
        assertTrue(uri.toString().contains("/imagebrowser"))
        verify(spyContext).packageName
    }

    @Test
    fun `given getSearchSuggestionsInstantAppIntent, should return proper intent with expected uri and args`() {
        /** Exercise **/
        val intent = HomePageFeatureModuleManager.getSearchSuggestionsInstantAppIntent(spyContext)

        /** Verify **/
        assertNotNull(intent)
        val uri = intent.toUri(URI_ALLOW_UNSAFE)
        assertNotNull(uri)
        assertTrue(uri.toString().contains("/search"))
        verify(spyContext).packageName
    }
}