package laurenyew.imagebrowser.search

import android.support.v4.app.Fragment
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManager
import laurenyew.imagebrowser.search.contracts.SearchFeatureModuleManagerContract
import laurenyew.imagebrowser.search.fragments.SearchSuggestionsFragment

object SearchFeatureModuleManager : FeatureModuleManager(), SearchFeatureModuleManagerContract.Views {
    //region Views
    override fun getSearchSuggestionsView(): Fragment = SearchSuggestionsFragment.newInstance()
    //endregion
}