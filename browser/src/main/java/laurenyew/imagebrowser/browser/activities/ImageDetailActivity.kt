package laurenyew.imagebrowser.browser.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.image_detail_activity.*
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.fragments.ImageDetailFragment

/**
 * An activity representing a single imageDetail detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ImageBrowserActivity].
 */
open class ImageDetailActivity : AppCompatActivity() {
    private val detailFragment = ImageDetailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_detail_activity)
        setSupportActionBar(toolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            detailFragment.apply {
                arguments = Bundle().apply {
                    putString(ImageDetailFragment.ARG_ITEM_ID, intent.getStringExtra(ImageDetailFragment.ARG_ITEM_ID))
                    putString(ImageDetailFragment.ARG_ITEM_IMAGE_URL, intent.getStringExtra(ImageDetailFragment.ARG_ITEM_IMAGE_URL))
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.imageDetailContainer, detailFragment)
                    .commit()
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
