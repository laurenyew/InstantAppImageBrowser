package laurenyew.imagebrowser.homepage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.home_page_activity.*
import laurenyew.imagebrowser.base.SharedPrefConfig

open class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page_activity)
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
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://laurenyew.imagebrowser.com/imagebrowser")).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                `package` = packageName
            })
        }
    }
}
