package laurenyew.imagebrowser.search.contracts

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

/**
 * @author Lauren Yew on 04/29/2018
 *
 * Not much needed here. The Search / Suggestions page is pretty simple.
 */
interface SearchFeatureModuleManagerContract {
    interface Views {
        fun getSearchSuggestionsView(): Fragment
    }

    interface InstantAppLinks {
        fun getImageBrowserInstantAppIntent(context: Context?, searchTerm: String?): Intent
    }
}