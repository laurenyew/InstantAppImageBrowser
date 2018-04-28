package laurenyew.imagebrowser.browser.contracts

import android.content.Context
import android.content.Intent

interface ImageBrowserFeatureModuleContract {

    //region Activities
    fun getImageDetailActivity(context: Context, itemId: String, itemImageUrl: String, itemTitle: String? = null): Intent
    //endregion

    //region Views
    fun getImageBrowserView(): ImageBrowserContract.View
    fun getImageDetailView(itemId: String, itemImageUrl: String, itemTitle: String? = null): ImageDetailContract.View
    //endregion

    //region Presenters
    fun getImageBrowserPresenter(): ImageBrowserContract.Presenter
    //endregion
}