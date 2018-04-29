package laurenyew.imagebrowser.base.model

/**
 * POJO Data Model (generic for image related data)
 * Available to be used in other APIs besides Flickr
 */
data class ImageData(val id: String, val title: String?, val imageUrl: String)