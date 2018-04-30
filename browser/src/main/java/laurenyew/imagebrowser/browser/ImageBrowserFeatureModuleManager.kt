package laurenyew.imagebrowser.browser

import android.content.Context
import android.content.Intent
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManager
import laurenyew.imagebrowser.browser.activities.ImageDetailActivity
import laurenyew.imagebrowser.browser.adapters.ImageBrowserRecyclerViewAdapter
import laurenyew.imagebrowser.browser.contracts.ImageBrowserContract
import laurenyew.imagebrowser.browser.contracts.ImageBrowserFeatureModuleContract
import laurenyew.imagebrowser.browser.contracts.ImageDetailContract
import laurenyew.imagebrowser.browser.fragments.ImageBrowserFragment
import laurenyew.imagebrowser.browser.fragments.ImageDetailFragment
import laurenyew.imagebrowser.browser.presenters.ImageBrowserPresenter

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * Feature Module Manager for Image Browser.
 * Makes it easy to provide your own / unit test swap out the parts that make up Image Browser.
 */
object ImageBrowserFeatureModuleManager : FeatureModuleManager(), ImageBrowserFeatureModuleContract.Activities, ImageBrowserFeatureModuleContract.Views, ImageBrowserFeatureModuleContract.Presenters, ImageBrowserFeatureModuleContract.Adapters {
    //region activities
    override fun getImageDetailActivity(context: Context, itemId: String, itemImageUrl: String, itemTitle: String?): Intent =
            ImageDetailActivity.newInstance(context, itemId, itemImageUrl, itemTitle)
    //endregion

    //region Views
    override fun getImageBrowserView(searchTerm: String?): ImageBrowserContract.View = ImageBrowserFragment.newInstance(searchTerm)

    override fun getImageDetailView(itemId: String, itemImageUrl: String, itemTitle: String?): ImageDetailContract.View = ImageDetailFragment.newInstance(itemId, itemImageUrl, itemTitle)
    //endregion

    //region Presenters
    override fun getImageBrowserPresenter(): ImageBrowserContract.Presenter = ImageBrowserPresenter()
    //endregion

    //region Adapters
    override fun getImageBrowserAdapter(presenter: ImageBrowserContract.Presenter?): ImageBrowserRecyclerViewAdapter = ImageBrowserRecyclerViewAdapter(presenter)
    //endregion
}