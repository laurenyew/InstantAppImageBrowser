package laurenyew.imagebrowser.browser.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.image_browser_activity.*
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.fragments.ImageBrowserFragment

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

        val fragment = ImageBrowserFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.imageBrowserFrameLayout, fragment, FRAGMENT_TAG)
                .commit()
    }
}
