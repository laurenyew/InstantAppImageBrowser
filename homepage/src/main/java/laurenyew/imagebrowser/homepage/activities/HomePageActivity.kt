package laurenyew.imagebrowser.homepage.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.home_page_activity.*
import laurenyew.imagebrowser.base.SharedPrefConfig
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManagerController
import laurenyew.imagebrowser.homepage.BuildConfig
import laurenyew.imagebrowser.homepage.HomePageFeatureModuleManager
import laurenyew.imagebrowser.homepage.R
import laurenyew.imagebrowser.homepage.contracts.HomePageFeatureModuleManagerContract

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * Simple activity to show the home page. No MVP required here.
 * InstantApp link Intents split out into the Feature Module so they can easily be replaced / found
 */
open class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page_activity)

        setupFeatureModuleManager()

        displayButtons()
    }

    open fun displayButtons() {
        //Debug Features
        if (BuildConfig.DEBUG) {
            //Default will not show recent items
            showRecentItemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                val sharedPreferences = applicationContext.getSharedPreferences(SharedPrefConfig.BROWSER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                sharedPreferences.edit().putBoolean(SharedPrefConfig.SHOULD_SHOW_RECENT_IMAGES, isChecked).apply()
            }
        } else {
            debugTextView.visibility = View.GONE
            showRecentItemCheckbox.visibility = View.GONE
            val sharedPreferences = applicationContext.getSharedPreferences(SharedPrefConfig.BROWSER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            sharedPreferences.edit().remove(SharedPrefConfig.SHOULD_SHOW_RECENT_IMAGES).apply()
        }

        openImageBrowserButton.setOnClickListener {
            val module: HomePageFeatureModuleManagerContract.InstantAppLinks = FeatureModuleManagerController.getFeatureModuleManager(HomePageFeatureModuleManagerContract.InstantAppLinks::class.java)
                    ?: HomePageFeatureModuleManager
            startActivity(module.getImageBrowserInstantAppIntent(applicationContext))
        }

        openSearchImageBrowserButton.setOnClickListener {
            val module: HomePageFeatureModuleManagerContract.InstantAppLinks = FeatureModuleManagerController.getFeatureModuleManager(HomePageFeatureModuleManagerContract.InstantAppLinks::class.java)
                    ?: HomePageFeatureModuleManager
            startActivity(module.getSearchSuggestionsInstantAppIntent(applicationContext))
        }
    }

    //region Helper Methods
    open fun setupFeatureModuleManager() {
        FeatureModuleManagerController.addFeatureModuleManager(HomePageFeatureModuleManager)
    }
    //endregion
}
