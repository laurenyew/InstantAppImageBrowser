package laurenyew.imagebrowser.browser.contracts

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * Image Detail was pretty simple. No extra data was needed to be loaded,
 * so just had a View contract for the argument keys being passed in
 */
interface ImageDetailContract{
    interface View{
        companion object {
            const val ARG_ITEM_ID = "item_id"
            const val ARG_ITEM_IMAGE_URL = "item_image_url"
            const val ARG_ITEM_IMAGE_TITLE = "item_image_title"
        }
    }
}