package laurenyew.imagebrowser.browser.fragments

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import laurenyew.imagebrowser.base.BuildConfig
import laurenyew.imagebrowser.browser.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startVisibleFragment

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ImageDetailFragmentTest {
    private lateinit var fragment: ImageDetailFragment

    private lateinit var imageDetailView: ImageView
    private lateinit var titleTextView: TextView

    private val expectedTitle = "expectedTitle"

    @Before
    fun setup() {
        //Setup up Picasso
        try {
            Picasso.get()
        } catch (e: IllegalStateException) {
            Picasso.setSingletonInstance(Picasso.Builder(RuntimeEnvironment.application.applicationContext).build())
        }
        fragment = startTestFragment()
        setupViews(fragment.view!!)
    }

    @After
    fun tearDown() {
        fragment.onDestroyView()
    }

    @Test
    fun `on view created, image is visible and title is shown`() {
        /** Verify **/
        assertEquals(View.VISIBLE, imageDetailView.visibility)
        assertEquals(View.VISIBLE, titleTextView.visibility)
        assertEquals(expectedTitle, titleTextView.text.toString())
    }

    //region Helper Methods
    private fun startTestFragment(): ImageDetailFragment {
        val fragment = ImageDetailFragment.newInstance("id", "url", expectedTitle)
        startVisibleFragment(fragment)
        return fragment
    }

    private fun setupViews(view: View) {
        imageDetailView = view.findViewById(R.id.detailImageView)
        titleTextView = view.findViewById(R.id.detailImageTitleTextView)
    }
    //endregion
}