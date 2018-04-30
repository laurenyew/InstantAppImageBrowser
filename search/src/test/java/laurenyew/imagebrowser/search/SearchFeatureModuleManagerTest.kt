package laurenyew.imagebrowser.search

import android.content.Context
import android.content.Intent
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import laurenyew.imagebrowser.base.BuildConfig
import laurenyew.imagebrowser.base.ImageBrowserConfig
import laurenyew.imagebrowser.search.fragments.SearchSuggestionsFragment
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class SearchFeatureModuleManagerTest {
    private val spyContext: Context = spy(RuntimeEnvironment.application.applicationContext)

    @Test
    fun `given getSearchSuggestionsView, should return SearchSuggestionsFragment`() {
        /** Exercise **/
        val view = SearchFeatureModuleManager.getSearchSuggestionsView()

        /** Verify **/
        assertNotNull(view)
        assertTrue(view is SearchSuggestionsFragment)
    }

    @Test
    fun `given getImageBrowserInstantAppIntent, should return proper intent with expected uri and args`() {
        /** Exercise **/
        val intent = SearchFeatureModuleManager.getImageBrowserInstantAppIntent(spyContext, "test")

        /** Verify **/
        assertNotNull(intent)
        assertEquals("test", intent.getStringExtra(ImageBrowserConfig.ARG_SEARCH_TERM))
        val uri = intent.toUri(Intent.URI_ALLOW_UNSAFE)
        assertNotNull(uri)
        assertTrue(uri.toString().contains("/imagebrowser"))
        verify(spyContext).packageName
    }
}
