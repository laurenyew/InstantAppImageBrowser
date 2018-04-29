package laurenyew.imagebrowser.search.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.search_suggestions_activity.*
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManagerList
import laurenyew.imagebrowser.search.R
import laurenyew.imagebrowser.search.SearchFeatureModuleManager
import laurenyew.imagebrowser.search.contracts.SearchFeatureModuleManagerContract

/**
 * Search Suggestions Feature
 * Has Search bar, and suggestions for search types
 */
open class SearchSuggestionsActivity : AppCompatActivity() {
    companion object {
        const val FRAGMENT_TAG = "searchSuggestions"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.search_suggestions_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.search_suggestions_page_title)

        setupFeatureModuleManager()

        //Show the view
        val module: SearchFeatureModuleManagerContract.Views = FeatureModuleManagerList.getFeatureModuleManager(SearchFeatureModuleManagerContract.Views::class.java)
                ?: SearchFeatureModuleManager
        val searchSuggestionsView = module.getSearchSuggestionsView()
        if (searchSuggestionsView is Fragment) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.searchSuggestionsFrameLayout, searchSuggestionsView, FRAGMENT_TAG)
                    .commit()
        }
    }

    open fun setupFeatureModuleManager() {
        FeatureModuleManagerList.addFeatureModuleManager(SearchFeatureModuleManager)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    navigateUpTo(Intent(this, SearchSuggestionsActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
