package tw.holidaybear.graphql.android.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Single
import tw.holidaybear.graphql.android.TrendQuery
import tw.holidaybear.graphql.android.type.SearchType
import java.text.SimpleDateFormat
import java.util.*

class TrendRepository(private val apolloClient: ApolloClient) {

    val repos = mutableListOf<Repo>()

    fun getRepos() = if (repos.isEmpty()) getFromRemote() else getFromCache()

    private fun getFromRemote(): Single<List<Repo>> {
        val lastWeekDate = Calendar.getInstance().timeInMillis - 24 * 3600 * 1000 * 7
        val formattedDateText = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(lastWeekDate)

        val trendQuery = TrendQuery.builder()
                .first(25)
                .query("created:>$formattedDateText sort:stars-desc")
                .type(SearchType.REPOSITORY)
                .build()

        return Rx2Apollo.from(apolloClient.query(trendQuery))
                .firstOrError()
                .map { response -> RepoMapper.toRepos(response.data()) }
                .doOnSuccess { data -> repos.addAll(data) }
    }

    private fun getFromCache(): Single<List<Repo>> {
        return Single.just(repos)
    }

    companion object {
        fun newInstance(apolloClient: ApolloClient) = TrendRepository(apolloClient)
    }
}