package laurenyew.imagebrowser.base.networking

import android.support.annotation.VisibleForTesting
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.cert.CertificateException
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Api Builder to keep the retrofit creation logic separate from the commands
 *
 * Note: Currently trusting all certs b/c was getting "CertificateRevokedException: Certificate has been revoked, reason: UNSPECIFIED"
 * b/c flickr certificates kept expiring for Android. Wouldn't put something like this in production,
 * but for a test-app, leaving for now.
 * Resource: https://futurestud.io/tutorials/retrofit-2-how-to-trust-unsafe-ssl-certificates-self-signed-expired
 */
object FlickrApiBuilder {
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val okHttpClient: OkHttpClient.Builder
    private val retrofit: Retrofit

    // Create a trust manager that does not validate certificate chains
    private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
    })

    init {
        okHttpClient = setupOkHttp()
        retrofit = Retrofit.Builder().baseUrl("https://api.flickr.com/services/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient.build()).build()
    }

    fun <T> apiBuilder(apiClazz: Class<T>): T? = retrofit.create(apiClazz)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setupOkHttp(): OkHttpClient.Builder {
        //Setup HttpClient
        val httpClientBuilder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)

        //Add headers
        httpClientBuilder.addInterceptor {
            val request = it.request()
                    .newBuilder()
                    .addHeader("Accept-Language", Locale.getDefault().language)
                    .build()
            it.proceed(request)
        }

        httpClientBuilder.followRedirects(true)
        httpClientBuilder.followSslRedirects(true)

        //TODO: Currently trusting all SSL Certs. This is not good for a production app
        //but since this is a testapp for now its okay.
        //Had to add this b/c Flickr has SSL issues

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory
        httpClientBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        httpClientBuilder.hostnameVerifier { _, _ -> true }

        return httpClientBuilder
    }
}