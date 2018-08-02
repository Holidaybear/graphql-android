package tw.holidaybear.graphql.android.util

import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import tw.holidaybear.graphql.android.data.TrendRepository
import java.util.concurrent.TimeUnit

object Injection {

    private const val GITHUB_GRAPHQL_ENDPOINT = "https://api.github.com/graphql"

    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(provideNetworkInterceptor())
                .build()
    }

    fun provideApolloClient(): ApolloClient {
        return ApolloClient.builder()
                .serverUrl(GITHUB_GRAPHQL_ENDPOINT)
                .okHttpClient(provideOkHttpClient())
                .build()
    }

    fun provideNetworkInterceptor(): Interceptor {
        return Interceptor { chain -> chain.proceed(chain.request().newBuilder().header("Authorization", "Bearer <TOKEN>").build()) }
    }

    fun provideTrendRepository(apolloClient: ApolloClient): TrendRepository {
        return TrendRepository(apolloClient)
    }

    fun provideViewModelFactory(): ViewModelFactory {
        val repository = provideTrendRepository(provideApolloClient())
        return ViewModelFactory(repository)
    }
}