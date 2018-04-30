package laurenyew.imagebrowser.base

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * ImageBrowser Shared Feature Config keys
 * Keys that are shared across feature modules
 */
class ImageBrowserConfig {
    companion object {
        //Used for passing search term between search and browser feature module(s)
        const val ARG_SEARCH_TERM = "arg_search_term"
    }
}