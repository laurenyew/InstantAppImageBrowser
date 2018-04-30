package laurenyew.imagebrowser.base.model

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * POJO Data Model (generic for image related data)
 * Available to be used in other APIs besides Flickr
 */
data class ImageData(val id: String, val title: String?, val imageUrl: String)