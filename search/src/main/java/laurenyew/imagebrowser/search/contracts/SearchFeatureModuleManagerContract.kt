package laurenyew.imagebrowser.search.contracts

import android.support.v4.app.Fragment

interface SearchFeatureModuleManagerContract {
    interface Views {
        fun getSearchSuggestionsView(): Fragment
    }
}