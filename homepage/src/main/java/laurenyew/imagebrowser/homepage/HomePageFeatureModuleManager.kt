package laurenyew.imagebrowser.homepage

import android.content.Context
import android.content.Intent
import android.net.Uri
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManager
import laurenyew.imagebrowser.homepage.contracts.HomePageFeatureModuleManagerContract

/**
 * @author Lauren Yew on 04/29/2018.
 */
object HomePageFeatureModuleManager : FeatureModuleManager(), HomePageFeatureModuleManagerContract.InstantAppLinks {
    override fun getImageBrowserInstantAppIntent(context: Context?): Intent =
            Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://laurenyew.imagebrowser.com/imagebrowser")).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                `package` = context?.packageName
            }

    override fun getSearchSuggestionsInstantAppIntent(context: Context?): Intent =
            Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://laurenyew.imagebrowser.com/search")).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                `package` = context?.packageName
            }
}