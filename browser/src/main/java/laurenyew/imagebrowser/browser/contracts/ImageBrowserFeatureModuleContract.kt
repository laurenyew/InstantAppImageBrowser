package laurenyew.imagebrowser.browser.contracts

import android.content.Context
import android.content.Intent
import laurenyew.imagebrowser.browser.adapters.ImageBrowserRecyclerViewAdapter

interface ImageBrowserFeatureModuleContract {

    interface Activities {
        fun getImageDetailActivity(context: Context, itemId: String, itemImageUrl: String, itemTitle: String? = null): Intent
    }

    interface Views {
        fun getImageBrowserView(searchTerm: String?): ImageBrowserContract.View

        fun getImageDetailView(itemId: String, itemImageUrl: String, itemTitle: String? = null): ImageDetailContract.View
    }

    interface Presenters {
        fun getImageBrowserPresenter(): ImageBrowserContract.Presenter
    }

    interface Adapters {
        fun getImageBrowserAdapter(presenter: ImageBrowserContract.Presenter?): ImageBrowserRecyclerViewAdapter
    }
}