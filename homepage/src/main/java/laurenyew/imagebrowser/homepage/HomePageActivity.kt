package laurenyew.imagebrowser.homepage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.home_page_activity.*

open class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page_activity)
        displayButtons()
    }

    open fun displayButtons() {
        openImageBrowserButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://laurenyew.imagebrowser.com/imagebrowser")).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
            })
        }
    }
}
