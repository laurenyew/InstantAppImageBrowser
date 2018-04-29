package laurenyew.imagebrowser.base.api.response

import com.squareup.moshi.Json

/**
 * JSON Responses expected from the flickr api
 * (https://www.flickr.com/services/api/flickr.photos.search.html)
 *
 * Using Moshi to parse them
 */

data class FlickrPhotosResponse(@Json(name = "photos") val pageResponse: FlickrPhotoPageResponse)

data class FlickrPhotoPageResponse(
        @Json(name = "page") val currentPageNum: Int,
        @Json(name = "pages") val totalNumPages: Int,
        @Json(name = "perpage") val numPhotosPerPage: Int,
        @Json(name = "total") val totalNumPhotosAcrossAllPages: String,
        @Json(name = "photo") val photos: List<FlickrImageDataResponse>
)

data class FlickrImageDataResponse(
        @Json(name = "id") val id: String,
        @Json(name = "owner") val owner: String,
        @Json(name = "secret") val secret: String,
        @Json(name = "server") val server: String,
        @Json(name = "farm") val farm: String,
        @Json(name = "title") val title: String?
)