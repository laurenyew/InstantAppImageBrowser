package laurenyew.imagebrowser.browser.contracts

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import laurenyew.imagebrowser.browser.adapters.ImageBrowserRecyclerViewAdapter

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

    //region Adapters
    fun getImageBrowserAdapter(presenter: ImageBrowserContract.Presenter?): ImageBrowserRecyclerViewAdapter
}