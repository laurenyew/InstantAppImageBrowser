package laurenyew.imagebrowser.search.fragments

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.view.View
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import laurenyew.imagebrowser.base.BuildConfig
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManager
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManagerController
import laurenyew.imagebrowser.search.R
import laurenyew.imagebrowser.search.SearchFeatureModuleManager
import laurenyew.imagebrowser.search.contracts.SearchFeatureModuleManagerContract
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
class SearchSuggestionsFragmentTest {
    private lateinit var fragment: SearchSuggestionsFragment
    private val mockModule: MockSearchFeatureModuleManager = spy(MockSearchFeatureModuleManager())
    private lateinit var allProductsCard: CardView
    private lateinit var suggestion1Card: CardView
    private lateinit var suggestion2Card: CardView
    private lateinit var suggestion3Card: CardView
    private lateinit var suggestion4Card: CardView

    @Before
    fun setup() {
        FeatureModuleManagerController.addFeatureModuleManager(mockModule)
        fragment = spy(startTestFragment())
        setupViews(fragment.view!!)
    }

    @After
    fun tearDown() {
        fragment.onDestroyView()
    }

    @Test
    fun `on view created, view is populated as expected`() {
        /** Verify **/
        assertEquals(View.VISIBLE, allProductsCard.visibility)
        assertEquals(View.VISIBLE, suggestion1Card.visibility)
        assertEquals(View.VISIBLE, suggestion2Card.visibility)
        assertEquals(View.VISIBLE, suggestion3Card.visibility)
        assertEquals(View.VISIBLE, suggestion4Card.visibility)
    }

    @Test
    fun `given perform search, gets the feature module's instant app link intent`() {
        /** Exercise **/
        fragment.performSearch("test")

        /** Verify **/
        verify(mockModule).getImageBrowserInstantAppIntent(any(), argThat { equals("test") })
    }

    //region Helper Methods
    private fun startTestFragment(): SearchSuggestionsFragment {
        val fragment = SearchSuggestionsFragment()
        startVisibleFragment(fragment)
        return fragment
    }

    private fun setupViews(view: View) {
        allProductsCard = view.findViewById(R.id.allProductsCardView)
        suggestion1Card = view.findViewById(R.id.suggestion1CardView)
        suggestion2Card = view.findViewById(R.id.suggestion2CardView)
        suggestion3Card = view.findViewById(R.id.suggestion3CardView)
        suggestion4Card = view.findViewById(R.id.suggestion4CardView)
    }

    open class MockSearchFeatureModuleManager : FeatureModuleManager(), SearchFeatureModuleManagerContract.InstantAppLinks {
        override fun getImageBrowserInstantAppIntent(context: Context?, searchTerm: String?): Intent = SearchFeatureModuleManager.getImageBrowserInstantAppIntent(context, searchTerm)
    }
    //endregion
}