package tw.holidaybear.graphql.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import tw.holidaybear.graphql.android.type.SearchType
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getIssues()
    }

    private fun getIssues() {

        val query = RecentQuery.builder()
                .first(2)
                .query("created:>2018-07-15 sort:stars-desc")
                .type(SearchType.REPOSITORY)
                .build()

        apolloClient.query(query).enqueue(object : ApolloCall.Callback<RecentQuery.Data>(){

            override fun onResponse(response: Response<RecentQuery.Data>) {
                val data = response.data()
                runOnUiThread {
                    result.text = data.toString()
                }
            }

            override fun onFailure(e: ApolloException) {
                runOnUiThread {
                    result.text = e.message
                }
            }
        })
    }

    companion object {

        private val GITHUB_GRAPHQL_ENDPOINT = "https://api.github.com/graphql"

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
