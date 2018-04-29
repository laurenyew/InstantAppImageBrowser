package laurenyew.imagebrowser.browser.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.image_browser_fragment.*
import laurenyew.imagebrowser.base.SharedPrefConfig
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManagerList
import laurenyew.imagebrowser.browser.ImageBrowserFeatureModuleManager
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.adapters.ImageBrowserRecyclerViewAdapter
import laurenyew.imagebrowser.browser.adapters.data.ImagePreviewDataWrapper
import laurenyew.imagebrowser.browser.contracts.ImageBrowserContract
import laurenyew.imagebrowser.browser.contracts.ImageBrowserFeatureModuleContract

class ImageBrowserFragment : Fragment(), ImageBrowserContract.View, SwipeRefreshLayout.OnRefreshListener {
    companion object {
        @JvmStatic
        fun newInstance(): ImageBrowserFragment = ImageBrowserFragment()
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

    private var adapter: ImageBrowserRecyclerViewAdapter? = null
    private var module: ImageBrowserFeatureModuleContract? = null
    private var presenter: ImageBrowserContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        module = FeatureModuleManagerList.getFeatureModuleManager(ImageBrowserFeatureModuleContract::class.java)
                ?: ImageBrowserFeatureModuleManager
        presenter = module?.getImageBrowserPresenter()
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
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter = null
        module = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.image_browser_menu, menu)
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
        if (isAdded && isVisible) {
            if (adapter == null) {
                adapter = module?.getImageBrowserAdapter(presenter)
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
        if (isAdded && isVisible) {
            Toast.makeText(context, R.string.image_browser_load_failed, Toast.LENGTH_LONG).show()
        }
    }

    override fun onShowImageDetail(itemId: String, itemImageUrl: String, itemTitle: String?) {
        if (isAdded && isVisible) {
            if (isRunningTwoPaneMode) {
                val module: ImageBrowserFeatureModuleContract = FeatureModuleManagerList.getFeatureModuleManager(ImageBrowserFeatureModuleContract::class.java)
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
                    val module: ImageBrowserFeatureModuleContract = FeatureModuleManagerList.getFeatureModuleManager(ImageBrowserFeatureModuleContract::class.java)
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