package laurenyew.imagebrowser.search.contracts

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

interface SearchFeatureModuleManagerContract {
    interface Views {
        fun getSearchSuggestionsView(): Fragment
    }

    interface InstantAppLinks {
        fun getImageBrowserInstantAppIntent(context: Context?, searchTerm: String?): Intent
    }
}