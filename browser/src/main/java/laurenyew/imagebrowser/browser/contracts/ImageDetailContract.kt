package laurenyew.imagebrowser.browser.contracts

interface ImageDetailContract{
    interface View{
        companion object {
            const val ARG_ITEM_ID = "item_id"
            const val ARG_ITEM_IMAGE_URL = "item_image_url"
            const val ARG_ITEM_IMAGE_TITLE = "item_image_title"
        }
    }
}