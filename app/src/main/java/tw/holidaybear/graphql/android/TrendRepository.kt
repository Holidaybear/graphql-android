package tw.holidaybear.graphql.android

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import tw.holidaybear.graphql.android.type.SearchType
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager

class TrendRepository {

    fun getTrends(): Observable<TrendQuery.Data> {

        val trendQuery = TrendQuery.builder()
                .first(2)
                .query("created:>2018-07-15 sort:stars-desc")
                .type(SearchType.REPOSITORY)
                .build()

        return Rx2Apollo.from(apolloClient.query(trendQuery))
                .map { response -> response.data() }
    }

    companion object {

        private const val GITHUB_GRAPHQL_ENDPOINT = "https://api.github.com/graphql"

        private val httpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .sslSocketFactory(SSL(trustAllCert), trustAllCert)
                    .addNetworkInterceptor(NetworkInterceptor())
                    .build()
        }


        private val apolloClient: ApolloClient by lazy {
            ApolloClient.builder()
                    .serverUrl(GITHUB_GRAPHQL_ENDPOINT)
                    .okHttpClient(httpClient)
                    .build()
        }

        private class NetworkInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): okhttp3.Response {
                return chain!!.proceed(chain.request().newBuilder().header("Authorization", "Bearer <TOKEN>").build())
            }
        }

        private val trustAllCert = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

        }
    }
}