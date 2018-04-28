package laurenyew.imagebrowser.browser.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.image_browser_activity.*
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManagerList
import laurenyew.imagebrowser.browser.ImageBrowserFeatureModuleManager
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.contracts.ImageBrowserFeatureModuleContract

/**
 * ImageBrowserActivity
 * Container for the ImageBrowserFragment
 */
open class ImageBrowserActivity : AppCompatActivity() {
    companion object {
        const val FRAGMENT_TAG = "imageDetailFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.image_browser_activity)

        setSupportActionBar(toolbar)
        toolbar.title = title

        setupFeatureModuleManager()

        //Show the view
        val module = FeatureModuleManagerList.getFeatureModuleManager(ImageBrowserFeatureModuleContract::class.java) ?: ImageBrowserFeatureModuleManager
        val browserView = module.getImageBrowserView()
        if (browserView is Fragment) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.imageBrowserFrameLayout, browserView, FRAGMENT_TAG)
                    .commit()
        }
    }

    open fun setupFeatureModuleManager() {
        FeatureModuleManagerList.addFeatureModuleManager(ImageBrowserFeatureModuleManager)
    }
}
