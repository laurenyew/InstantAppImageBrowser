package laurenyew.imagebrowser.browser.fragments

import android.app.SearchManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING
import android.support.v7.widget.SearchView
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.image_browser_fragment.*
import laurenyew.imagebrowser.base.ImageBrowserConfig
import laurenyew.imagebrowser.base.SharedPrefConfig
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManagerList
import laurenyew.imagebrowser.browser.ImageBrowserFeatureModuleManager
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.adapters.ImageBrowserRecyclerViewAdapter
import laurenyew.imagebrowser.browser.adapters.data.ImagePreviewDataWrapper
import laurenyew.imagebrowser.browser.contracts.ImageBrowserContract
import laurenyew.imagebrowser.browser.contracts.ImageBrowserFeatureModuleContract
import java.util.*

open class ImageBrowserFragment : Fragment(), ImageBrowserContract.View, SwipeRefreshLayout.OnRefreshListener {
    companion object {
        @JvmStatic
        fun newInstance(searchTerm: String? = null): ImageBrowserFragment =
                ImageBrowserFragment().apply {
                    val bundle = Bundle()
                    if (searchTerm != null) {
                        bundle.putString(ImageBrowserConfig.ARG_SEARCH_TERM, searchTerm)
                    }
                    arguments = bundle
                }

        //Wait for .5 secs (avg user typing time is .3-1 sec per character)
        const val DEFAULT_SEARCH_DELAY = 500L
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var isRunningTwoPaneMode: Boolean = false
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var shouldShowFirstItem = true

    private var searchTerm: String? = null
    private var isFirstTimeSearch = false

    private var searchMenuItem: MenuItem? = null

    private var searchView: SearchView? = null
    private var searchQueryKeyEntryTimer: Timer? = null

    private var adapter: ImageBrowserRecyclerViewAdapter? = null
    private var presenter: ImageBrowserContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        searchTerm = arguments?.getString(ImageBrowserConfig.ARG_SEARCH_TERM)
        isFirstTimeSearch = searchTerm?.isNotEmpty() == true

        val module: ImageBrowserFeatureModuleContract.Presenters = FeatureModuleManagerList.getFeatureModuleManager(ImageBrowserFeatureModuleContract.Presenters::class.java)
                ?: ImageBrowserFeatureModuleManager
        presenter = module.getImageBrowserPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.image_browser_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageBrowserRecyclerView.apply {
            //Use columns if in Portrait, Rows if in landscape
            val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            layoutManager = GridLayoutManager(context, 2, if (isLandscape) GridLayoutManager.HORIZONTAL else GridLayoutManager.VERTICAL, false)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if ((isLandscape && !recyclerView.canScrollHorizontally(1)) || (!isLandscape && !recyclerView.canScrollVertically(1))) {
                        presenter?.loadNextPageOfImages()
                    }
                    if (newState == SCROLL_STATE_DRAGGING && searchView?.isIconified == false) {
                        //Hide Keyboard
                        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                        imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }
            })
        }

        imageBrowserSwipeRefreshLayout.isRefreshing = true
        imageBrowserSwipeRefreshLayout.setOnRefreshListener(this)

        // The detail container view will be present only in the
        // large-screen layouts (res/values-w900dp).
        // If this view is present, then the
        // activity should be in two-pane mode.
        if (imageDetailContainer != null) {
            isRunningTwoPaneMode = true
        }

        presenter?.onBind(this, context)
        onRefresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.unBind()

        searchQueryKeyEntryTimer?.cancel()
        searchQueryKeyEntryTimer = null

        searchMenuItem = null
        searchView = null
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.image_browser_menu, menu)
        searchMenuItem = menu.findItem(R.id.menu_search)

        val menuView = searchMenuItem?.actionView
        if (menuView is SearchView?) {
            searchView = menuView
            searchView?.queryHint = context?.getString(laurenyew.imagebrowser.base.R.string.search_hint)
            val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE)
            if (searchManager is SearchManager?) {
                searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity?.componentName))
            }
            searchView?.setOnQueryTextListener(getQueryTextListener())
            searchView?.setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    searchMenuItem?.collapseActionView()
                } else {
                    searchView?.isIconified = true
                }
            }

            if (isFirstTimeSearch) {
                searchView?.isIconified = false
                searchView?.setQuery(searchTerm ?: "", false)
            }
        }

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh) {
            imageBrowserSwipeRefreshLayout.isRefreshing = true
            onRefresh()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //region MVP
    /**
     * Load the images into the view
     */
    override fun onImagesLoaded(data: List<ImagePreviewDataWrapper>?) {
        isFirstTimeSearch = false
        if (isAdded && isVisible) {
            if (adapter == null) {
                val module: ImageBrowserFeatureModuleContract.Adapters = FeatureModuleManagerList.getFeatureModuleManager(ImageBrowserFeatureModuleContract.Adapters::class.java)
                        ?: ImageBrowserFeatureModuleManager
                adapter = module.getImageBrowserAdapter(presenter)
                imageBrowserRecyclerView.adapter = adapter
            }
            adapter?.updateData(data)
            imageBrowserSwipeRefreshLayout.isRefreshing = false

            if (data == null || data.isEmpty()) {
                imageBrowserEmptyTextView.visibility = View.VISIBLE
            } else if (shouldShowFirstItem) {
                //if this view has just been refreshed, we should
                //show the first item
                val firstItem = data[0]
                presenter?.onSelectPreview(firstItem.id, firstItem.imageUrl, firstItem.imageTitle)
                shouldShowFirstItem = false
            }
        }
    }

    override fun onImagesFailedToLoad() {
        isFirstTimeSearch = false
        if (isAdded && isVisible) {
            Toast.makeText(context, R.string.image_browser_load_failed, Toast.LENGTH_LONG).show()
        }
    }

    override fun onShowImageDetail(itemId: String, itemImageUrl: String, itemTitle: String?) {
        if (isAdded && isVisible) {
            if (isRunningTwoPaneMode && imageDetailContainer != null) {
                val module = FeatureModuleManagerList.getFeatureModuleManager(ImageBrowserFeatureModuleContract.Views::class.java)
                        ?: ImageBrowserFeatureModuleManager
                val detailView = module.getImageDetailView(itemId, itemImageUrl, itemTitle)
                if (detailView is Fragment) {
                    activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.imageDetailContainer, detailView)
                            ?.commit()
                }
            } else {
                val context = context
                if (context != null) {
                    val module: ImageBrowserFeatureModuleContract.Activities = FeatureModuleManagerList.getFeatureModuleManager(ImageBrowserFeatureModuleContract.Activities::class.java)
                            ?: ImageBrowserFeatureModuleManager
                    val intent = module.getImageDetailActivity(context, itemId, itemImageUrl, itemTitle)
                    context.startActivity(intent)
                }
            }
        }
    }
    //endregion

    //region SwipeRefreshLayout
    override fun onRefresh() {
        shouldShowFirstItem = isRunningTwoPaneMode
        presenter?.refreshImages(searchTerm ?: getDefaultSearchTerm())
    }
    //endregion

    //region Helper Methods
    /**
     * Available to override if the don't like the query behavior
     */
    open fun getQueryTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!isFirstTimeSearch) {
                    //Only search after the user has finished/slowed typing to avoid quick typing
                    //and running multiple searches
                    searchQueryKeyEntryTimer?.cancel()
                    searchQueryKeyEntryTimer = Timer()
                    searchQueryKeyEntryTimer?.schedule(object : TimerTask() {
                        override fun run() {
                            activity?.runOnUiThread {
                                if (isAdded && isVisible) {
                                    searchTerm = if (newText?.isNotEmpty() == true) newText else null
                                    onRefresh()
                                }
                            }
                        }
                    }, DEFAULT_SEARCH_DELAY)
                }
                return true
            }

        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getDefaultSearchTerm(): String {
        val context = context
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