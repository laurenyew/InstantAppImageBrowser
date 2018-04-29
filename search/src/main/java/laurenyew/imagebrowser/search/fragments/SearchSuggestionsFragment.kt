package laurenyew.imagebrowser.search.fragments

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import kotlinx.android.synthetic.main.search_suggestions_fragment.*
import laurenyew.imagebrowser.base.ImageBrowserConfig
import laurenyew.imagebrowser.search.R
import java.util.*

class SearchSuggestionsFragment : Fragment() {
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
        allProductsContainer?.setOnClickListener {
            searchView?.clearFocus()
            performSearch(null)
        }
        suggestion1Container?.setOnClickListener {
            searchView?.clearFocus()
            performSearch(getString(R.string.suggestion_button_title_1))
        }
        suggestion2Container?.setOnClickListener {
            searchView?.clearFocus()
            performSearch(getString(R.string.suggestion_button_title_2))
        }
        suggestion3Container?.setOnClickListener {
            searchView?.clearFocus()
            performSearch(getString(R.string.suggestion_button_title_3))
        }
        suggestion4Container?.setOnClickListener {
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
        startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://laurenyew.imagebrowser.com/imagebrowser")).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
            `package` = activity?.packageName
            if (searchTerm != null) {
                putExtra(ImageBrowserConfig.ARG_SEARCH_TERM, searchTerm.toLowerCase(Locale.US))
            }
        })
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