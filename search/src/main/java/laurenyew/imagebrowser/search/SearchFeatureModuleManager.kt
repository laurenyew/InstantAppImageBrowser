package laurenyew.imagebrowser.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import laurenyew.imagebrowser.base.ImageBrowserConfig
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManager
import laurenyew.imagebrowser.search.contracts.SearchFeatureModuleManagerContract
import laurenyew.imagebrowser.search.fragments.SearchSuggestionsFragment
import java.util.*

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * Feature Module Manager for Search / Suggestions feature
 */
object SearchFeatureModuleManager : FeatureModuleManager(), SearchFeatureModuleManagerContract.Views, SearchFeatureModuleManagerContract.InstantAppLinks {
    //region Views
    override fun getSearchSuggestionsView(): Fragment = SearchSuggestionsFragment.newInstance()
    //endregion

    //region InstantAppLinks
    override fun getImageBrowserInstantAppIntent(context: Context?, searchTerm: String?): Intent =
            Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://laurenyew.imagebrowser.com/imagebrowser")).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                `package` = context?.packageName
                if (searchTerm != null) {
                    putExtra(ImageBrowserConfig.ARG_SEARCH_TERM, searchTerm.toLowerCase(Locale.US))
                }
            }
    //endregion
}