package tw.holidaybear.graphql.android

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import tw.holidaybear.graphql.android.type.SearchType
import java.util.concurrent.TimeUnit

class TrendRepository {

    fun getTrends(): Observable<List<Repo>> {

        val trendQuery = TrendQuery.builder()
                .first(2)
                .query("created:>2018-07-15 sort:stars-desc")
                .type(SearchType.REPOSITORY)
                .build()

        return Rx2Apollo.from(apolloClient.query(trendQuery))
                .map { response -> RepoMapper.toRepos(response.data()) }
    }

    companion object {

        private const val GITHUB_GRAPHQL_ENDPOINT = "https://api.github.com/graphql"

        private val httpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
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
    }
}