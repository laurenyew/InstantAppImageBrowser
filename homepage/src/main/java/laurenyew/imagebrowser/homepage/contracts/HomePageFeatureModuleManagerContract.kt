package laurenyew.imagebrowser.homepage.contracts

import android.content.Context
import android.content.Intent

/**
 * @author Lauren Yew on 04/29/2018.
 */
interface HomePageFeatureModuleManagerContract {
    interface InstantAppLinks {
        fun getImageBrowserInstantAppIntent(context: Context?): Intent
        fun getSearchSuggestionsInstantAppIntent(context: Context?): Intent
    }
}