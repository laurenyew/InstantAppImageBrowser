package laurenyew.imagebrowser.browser.fragments

import android.content.Context
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.nhaarman.mockito_kotlin.*
import laurenyew.imagebrowser.base.BuildConfig
import laurenyew.imagebrowser.base.SharedPrefConfig
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.adapters.data.ImagePreviewDataWrapper
import laurenyew.imagebrowser.browser.helpers.ImageBrowserTestBase
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startVisibleFragment

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ImageBrowserFragmentTest : ImageBrowserTestBase() {
    private lateinit var fragment: ImageBrowserFragment
    private lateinit var pullRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyTextView: TextView

    override fun setup() {
        super.setup()
        fragment = startTestFragment()
        setupViews(fragment.view!!)
    }

    @After
    fun tearDown() {
        val sharedPrefs = context.getSharedPreferences(SharedPrefConfig.BROWSER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPrefs.edit().clear().commit()
        fragment.onDestroyView()
    }

    @Test
    fun `on view created, presenter should be bound and refreshed`() {
        /** Verify **/
        verify(mockPresenter).onBind(any(), any())
        verify(mockPresenter).refreshImages(context.getString(R.string.image_browser_base_search_term))
    }

    @Test
    fun `on view destroyed, presenter should be unbound`() {
        /** Exercise **/
        fragment.onDestroyView()

        /** Verify **/
        verify(mockPresenter).unBind()
    }

    @Test
    fun `on view pulled to refresh, presenter should be refreshed`() {
        /** Exercise **/
        fragment.onRefresh()

        /** Verify **/
        verify(mockPresenter, times(2)).refreshImages(context.getString(R.string.image_browser_base_search_term))
    }

    @Test
    fun `on view created, pull to refresh view should show`() {
        /** Verify **/
        assertEquals(View.VISIBLE, pullRefreshLayout.visibility)
        assertTrue(pullRefreshLayout.isRefreshing)
        assertEquals(View.VISIBLE, recyclerView.visibility)
        assertEquals(View.GONE, emptyTextView.visibility)
    }


    @Test
    fun `when images loaded into view first time in regular mode, recycler view updated, pull to refresh hidden, empty text hidden`() {
        /** Arrange **/
        val data = arrayListOf(ImagePreviewDataWrapper("id", "url", "title"))
        fragment.isRunningTwoPaneMode = false

        /** Exercise **/
        fragment.onImagesLoaded(data)
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        setupViews(fragment.view!!)

        /** Verify **/
        verify(mockAdapter).updateData(data)
        assertEquals(View.VISIBLE, pullRefreshLayout.visibility)
        assertFalse(pullRefreshLayout.isRefreshing)
        assertEquals(View.VISIBLE, recyclerView.visibility)
        assertEquals(View.GONE, emptyTextView.visibility)
        verify(mockPresenter, never()).onSelectPreview(any(), any(), anyOrNull())
    }


    @Test
    fun `when images loaded into view first time in two pane mode, recycler view updated, pull to refresh hidden, empty text hidden`() {
        /** Arrange **/
        val data = arrayListOf(ImagePreviewDataWrapper("id", "url", "title"))
        fragment.isRunningTwoPaneMode = true

        /** Exercise **/
        fragment.onRefresh()
        fragment.onImagesLoaded(data)
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        setupViews(fragment.view!!)

        /** Verify **/
        verify(mockAdapter).updateData(data)
        assertEquals(View.VISIBLE, pullRefreshLayout.visibility)
        assertFalse(pullRefreshLayout.isRefreshing)
        assertEquals(View.VISIBLE, recyclerView.visibility)
        assertEquals(View.GONE, emptyTextView.visibility)
        verify(mockPresenter).onSelectPreview("id", "url", "title")
    }


    @Test
    fun `when images loaded into view after first time in two pane mode, recycler view updated, pull to refresh hidden, empty text hidden`() {
        /** Arrange **/
        val data = arrayListOf(ImagePreviewDataWrapper("id", "url", "title"))
        fragment.isRunningTwoPaneMode = true

        /** Exercise **/
        fragment.shouldShowFirstItem = false
        fragment.onImagesLoaded(data)
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        setupViews(fragment.view!!)

        /** Verify **/
        verify(mockAdapter).updateData(data)
        assertEquals(View.VISIBLE, pullRefreshLayout.visibility)
        assertFalse(pullRefreshLayout.isRefreshing)
        assertEquals(View.VISIBLE, recyclerView.visibility)
        assertNotNull(recyclerView.adapter)
        assertEquals(View.GONE, emptyTextView.visibility)
        verify(mockPresenter, never()).onSelectPreview("id", "url", "title")
    }

    @Test
    fun `when no images loaded into view, recycler view updated, pull to refresh hidden, empty text should show`() {
        /** Arrange **/
        val data = ArrayList<ImagePreviewDataWrapper>()

        /** Exercise **/
        fragment.onImagesLoaded(data)
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        setupViews(fragment.view!!)

        /** Verify **/
        verify(mockAdapter).updateData(data)
        assertEquals(View.VISIBLE, pullRefreshLayout.visibility)
        assertFalse(pullRefreshLayout.isRefreshing)
        assertEquals(View.VISIBLE, recyclerView.visibility)
        assertEquals(View.VISIBLE, emptyTextView.visibility)
    }


    @Test
    fun `when image load fails, empty view should show`() {
        /** Exercise **/
        fragment.onImagesLoaded(null)
        fragment.onImagesFailedToLoad()
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        setupViews(fragment.view!!)

        /** Verify **/
        verify(mockAdapter).updateData(null)
        assertEquals(View.VISIBLE, pullRefreshLayout.visibility)
        assertFalse(pullRefreshLayout.isRefreshing)
        assertEquals(View.VISIBLE, recyclerView.visibility)
        assertEquals(View.VISIBLE, emptyTextView.visibility)
    }

    @Test
    fun `when fragment shows detail view and is two pane view, will get the detail view fragment`() {
        /** Arrange **/
        whenever(mockFeatureModule.getImageDetailActivity(any(), any(), any(), anyOrNull())).thenReturn(Intent())
        fragment.isRunningTwoPaneMode = true

        /** Exercise **/
        fragment.onShowImageDetail("id", "itemUrl", "itemTitle")

        /** Verify **/
        //Unfortunately, shouldn't get the detail view b/c the layout isn't available
        verify(mockFeatureModule, never()).getImageDetailView(any(), any(), any())
        verify(mockFeatureModule).getImageDetailActivity(any(), any(), any(), anyOrNull())
    }

    @Test
    fun `when fragment shows detail view and is regular view, will get the detail view activity`() {
        /** Arrange **/
        whenever(mockFeatureModule.getImageDetailActivity(any(), any(), any(), anyOrNull())).thenReturn(Intent())
        fragment.isRunningTwoPaneMode = false

        /** Exercise **/
        fragment.onShowImageDetail("id", "itemUrl", "itemTitle")

        /** Verify **/
        verify(mockFeatureModule).getImageDetailActivity(any(), any(), any(), anyOrNull())
        verify(mockFeatureModule, never()).getImageBrowserView(anyOrNull())
    }

    @Test
    fun `when sharedPrefs doesn't have SHOULD_SHOW_RECENT_IMAGES, getDefaultSearchTerm should return the default search term`() {
        /** Arrange **/
        val expectedSearchTerm = context.getString(R.string.image_browser_base_search_term)
        val sharedPrefs = context.getSharedPreferences(SharedPrefConfig.BROWSER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPrefs.edit().clear().commit()

        /** Exercise **/
        val searchTerm = fragment.getDefaultSearchTerm()

        /** Verify **/
        assertEquals(expectedSearchTerm, searchTerm)
    }

    @Test
    fun `when sharedPrefs SHOULD_SHOW_RECENT_IMAGES is false, getDefaultSearchTerm should return the default search term`() {
        /** Arrange **/
        val expectedSearchTerm = context.getString(R.string.image_browser_base_search_term)
        val sharedPrefs = context.getSharedPreferences(SharedPrefConfig.BROWSER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(SharedPrefConfig.SHOULD_SHOW_RECENT_IMAGES, false).commit()

        /** Exercise **/
        val searchTerm = fragment.getDefaultSearchTerm()

        /** Verify **/
        assertEquals(expectedSearchTerm, searchTerm)
    }

    @Test
    fun `when sharedPrefs SHOULD_SHOW_RECENT_IMAGES is true, getDefaultSearchTerm should return an empty string`() {
        /** Arrange **/
        val expectedSearchTerm = ""
        val sharedPrefs = context.getSharedPreferences(SharedPrefConfig.BROWSER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(SharedPrefConfig.SHOULD_SHOW_RECENT_IMAGES, true).commit()

        /** Exercise **/
        val searchTerm = fragment.getDefaultSearchTerm()

        /** Verify **/
        assertEquals(expectedSearchTerm, searchTerm)
    }

    //region Helper Methods
    private fun startTestFragment(): ImageBrowserFragment {
        val fragment = ImageBrowserFragment()
        startVisibleFragment(fragment)
        return fragment
    }

    private fun setupViews(view: View) {
        pullRefreshLayout = view.findViewById(R.id.imageBrowserSwipeRefreshLayout)
        recyclerView = view.findViewById(R.id.imageBrowserRecyclerView)
        emptyTextView = view.findViewById(R.id.imageBrowserEmptyTextView)
    }
    //endregion
}