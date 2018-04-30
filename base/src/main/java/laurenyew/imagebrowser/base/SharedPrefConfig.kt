package laurenyew.imagebrowser.base

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * Shared Preferences Keys Shared across feature modules
 */
class SharedPrefConfig {
    companion object {
        //Main shared preferences category
        const val BROWSER_SHARED_PREFERENCES = "browser_shared_preferences"

        //Debug Feature - choose whether to show recent images on empty search
        //or to use default search term
        const val SHOULD_SHOW_RECENT_IMAGES = "should_show_recent_images"
    }
}