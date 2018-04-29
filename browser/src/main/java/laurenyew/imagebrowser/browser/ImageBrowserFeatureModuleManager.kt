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

object ImageBrowserFeatureModuleManager : FeatureModuleManager(), ImageBrowserFeatureModuleContract {
    //region activities
    override fun getImageDetailActivity(context: Context, itemId: String, itemImageUrl: String, itemTitle: String?): Intent =
            ImageDetailActivity.newInstance(context, itemId, itemImageUrl, itemTitle)
    //endregion

    //region Views
    override fun getImageBrowserView(): ImageBrowserContract.View = ImageBrowserFragment.newInstance()

    override fun getImageDetailView(itemId: String, itemImageUrl: String, itemTitle: String?): ImageDetailContract.View = ImageDetailFragment.newInstance(itemId, itemImageUrl, itemTitle)
    //endregion

    //region Presenters
    override fun getImageBrowserPresenter(): ImageBrowserContract.Presenter = ImageBrowserPresenter()
    //endregion

    //region Adapters
    override fun getImageBrowserAdapter(presenter: ImageBrowserContract.Presenter?): ImageBrowserRecyclerViewAdapter = ImageBrowserRecyclerViewAdapter(presenter)
    //endregion
}