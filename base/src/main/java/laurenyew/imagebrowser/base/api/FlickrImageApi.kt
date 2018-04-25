package laurenyew.imagebrowser.base.api

import laurenyew.imagebrowser.base.api.response.FlickrPhotosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FlickrImageApi {
    @GET("rest/?method=flickr.photos.search&format=json&&nojsoncallback=1")
    fun searchPhotos(@Query("api_key") apiKey: String,
                     @Query("tags") searchTerm: String,
                     @Query("per_page") numPhotosPerPage: Int,
                     @Query("page") pageNum: Int = 1,
                     @QueryMap queryArgs: Map<String, String>? = null)
            : Call<FlickrPhotosResponse?>?
}