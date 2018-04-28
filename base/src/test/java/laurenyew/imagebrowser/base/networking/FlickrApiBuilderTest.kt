package laurenyew.imagebrowser.base.networking

import laurenyew.imagebrowser.base.BuildConfig
import laurenyew.imagebrowser.base.api.FlickrImageApi
import okhttp3.OkHttpClient
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class FlickrApiBuilderTest {
    @Test
    fun `given apiBuilder, api builder should return the retrofit api requested`() {
        /** Exercise **/
        val imageApi = FlickrApiBuilder.apiBuilder(FlickrImageApi::class.java)

        /** Verify **/
        assertNotNull(imageApi)
    }

    @Test
    fun `given setupOkHttp, OkHttpClientBuilder returned should be set up as expected`() {
        /** Exercise **/
        val httpBuilder: OkHttpClient.Builder = FlickrApiBuilder.setupOkHttp()
        val okHttpClient = httpBuilder.build()

        /** Verify **/
        assertNotNull(okHttpClient)
        assertEquals(2, okHttpClient.interceptors().size)
        assertTrue(okHttpClient.followRedirects())
        assertTrue(okHttpClient.followSslRedirects())
    }
}