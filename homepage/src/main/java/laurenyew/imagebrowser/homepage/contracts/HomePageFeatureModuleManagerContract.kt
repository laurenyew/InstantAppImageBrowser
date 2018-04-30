package laurenyew.imagebrowser.homepage.contracts

import android.content.Context
import android.content.Intent

interface HomePageFeatureModuleManagerContract {
    interface InstantAppLinks {
        fun getImageBrowserInstantAppIntent(context: Context?): Intent
        fun getSearchSuggestionsInstantAppIntent(context: Context?): Intent
    }
}