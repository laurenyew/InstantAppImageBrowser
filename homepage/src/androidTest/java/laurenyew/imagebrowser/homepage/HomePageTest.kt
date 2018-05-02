package laurenyew.imagebrowser.homepage

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import laurenyew.imagebrowser.homepage.activities.HomePageActivity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomePageTest {

    @JvmField
    @Rule
    val activity = ActivityTestRule<HomePageActivity>(HomePageActivity::class.java)

    @Test
    fun testHomePageOpened() {
        // Verify package name
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("laurenyew.imagebrowser.homepage.test", appContext.packageName)

        //Make sure views are visible
        onView(withId(R.id.openImageBrowserButton)).check(matches(isDisplayed()))
        onView(withId(R.id.openSearchImageBrowserButton)).check(matches(isDisplayed()))
        onView(withId(R.id.debugTextView)).check(matches(isDisplayed()))
                .check(matches(withText(R.string.debug)))
        onView(withId(R.id.showRecentItemCheckbox)).check(matches(isDisplayed()))
    }

    @Test
    fun testRecentItemCheckboxChangesSharedPref() {
        //TODO
    }
}
