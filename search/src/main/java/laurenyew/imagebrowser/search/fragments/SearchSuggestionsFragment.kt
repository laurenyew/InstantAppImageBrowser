package laurenyew.imagebrowser.search.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import kotlinx.android.synthetic.main.search_suggestions_fragment.*
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManagerController
import laurenyew.imagebrowser.search.R
import laurenyew.imagebrowser.search.SearchFeatureModuleManager
import laurenyew.imagebrowser.search.contracts.SearchFeatureModuleManagerContract

open class SearchSuggestionsFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): SearchSuggestionsFragment = SearchSuggestionsFragment()
    }

    private var searchMenuItem: MenuItem? = null

    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.search_suggestions_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        allProductsCardView?.setOnClickListener {
            searchView?.clearFocus()
            performSearch(null)
        }
        suggestion1CardView?.setOnClickListener {
            searchView?.clearFocus()
            performSearch(getString(R.string.suggestion_button_title_1))
        }
        suggestion2CardView?.setOnClickListener {
            searchView?.clearFocus()
            performSearch(getString(R.string.suggestion_button_title_2))
        }
        suggestion3CardView?.setOnClickListener {
            searchView?.clearFocus()
            performSearch(getString(R.string.suggestion_button_title_3))
        }
        suggestion4CardView?.setOnClickListener {
            searchView?.clearFocus()
            performSearch(getString(R.string.suggestion_button_title_4))
        }
        view.setOnTouchListener { view, motionEvent ->
            searchView?.clearFocus()
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchMenuItem = null
        searchView = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_suggestions_menu, menu)
        searchMenuItem = menu.findItem(R.id.menu_search)

        val menuView = searchMenuItem?.actionView
        if (menuView is SearchView?) {
            searchView = menuView
            searchView?.queryHint = context?.getString(laurenyew.imagebrowser.base.R.string.search_hint)
            val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE)
            if (searchManager is SearchManager?) {
                searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity?.componentName))
            }
            searchView?.setOnQueryTextListener(getQueryTextListener())
            searchView?.setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    searchMenuItem?.collapseActionView()
                } else {
                    searchView?.isIconified = true
                }
            }
        }

        return super.onCreateOptionsMenu(menu, inflater)
    }

    //region Helper Methods
    open fun performSearch(searchTerm: String?) {
        val module: SearchFeatureModuleManagerContract.InstantAppLinks = FeatureModuleManagerController.getFeatureModuleManager(SearchFeatureModuleManagerContract.InstantAppLinks::class.java)
                ?: SearchFeatureModuleManager
        startActivity(module.getImageBrowserInstantAppIntent(context, searchTerm))
    }

    /**
     * Available to override if the don't like the query behavior
     */
    open fun getQueryTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView?.clearFocus()
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        }
    }
    //endregion
}