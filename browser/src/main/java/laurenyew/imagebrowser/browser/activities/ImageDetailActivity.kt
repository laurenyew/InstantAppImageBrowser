package laurenyew.imagebrowser.browser.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.image_detail_activity.*
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManagerList
import laurenyew.imagebrowser.browser.ImageBrowserFeatureModuleManager
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.contracts.ImageBrowserFeatureModuleContract
import laurenyew.imagebrowser.browser.contracts.ImageDetailContract

/**
 * An activity representing a single imageDetail detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ImageBrowserActivity].
 */
open class ImageDetailActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        fun newInstance(context: Context, itemId: String, itemImageUrl: String, itemTitle: String? = null): Intent =
                Intent(context, ImageDetailActivity::class.java).apply {
                    putExtra(ImageDetailContract.View.ARG_ITEM_ID, itemId)
                    putExtra(ImageDetailContract.View.ARG_ITEM_IMAGE_URL, itemImageUrl)
                    if (itemTitle != null) {
                        putExtra(ImageDetailContract.View.ARG_ITEM_IMAGE_TITLE, itemTitle)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_detail_activity)
        setSupportActionBar(toolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val module: ImageBrowserFeatureModuleContract = FeatureModuleManagerList.getFeatureModuleManager(ImageBrowserFeatureModuleContract::class.java)
                    ?: ImageBrowserFeatureModuleManager
            val itemId = intent.getStringExtra(ImageDetailContract.View.ARG_ITEM_ID)
            val imageUrl = intent.getStringExtra(ImageDetailContract.View.ARG_ITEM_IMAGE_URL)
            val itemTitle = intent.getStringExtra(ImageDetailContract.View.ARG_ITEM_IMAGE_TITLE)
            val detailFragment = module.getImageDetailView(itemId, imageUrl, itemTitle)

            if (detailFragment is Fragment) {
                supportFragmentManager.beginTransaction()
                        .add(R.id.imageDetailContainer, detailFragment)
                        .commit()
            }
        }
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