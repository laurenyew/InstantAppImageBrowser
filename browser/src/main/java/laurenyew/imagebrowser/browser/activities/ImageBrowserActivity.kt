package laurenyew.imagebrowser.browser.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = getString(R.string.image_browser_title)

        setupFeatureModuleManager()

        //Show the view
        val module: ImageBrowserFeatureModuleContract = FeatureModuleManagerList.getFeatureModuleManager(ImageBrowserFeatureModuleContract::class.java)
                ?: ImageBrowserFeatureModuleManager
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

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    navigateUpTo(Intent(this, ImageBrowserActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
