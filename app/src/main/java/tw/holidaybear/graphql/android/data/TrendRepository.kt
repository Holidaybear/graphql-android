package tw.holidaybear.graphql.android.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import tw.holidaybear.graphql.android.TrendQuery
import tw.holidaybear.graphql.android.type.SearchType
import java.text.SimpleDateFormat
import java.util.*

class TrendRepository(private val apolloClient: ApolloClient) {

    fun getTrends(): Observable<List<Repo>> {
        val lastWeekDate = Calendar.getInstance().timeInMillis - 24 * 3600 * 1000 * 7
        val formattedDateText = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(lastWeekDate)

        val trendQuery = TrendQuery.builder()
                .first(25)
                .query("created:>$formattedDateText sort:stars-desc")
                .type(SearchType.REPOSITORY)
                .build()

        return Rx2Apollo.from(apolloClient.query(trendQuery))
                .map { response -> RepoMapper.toRepos(response.data()) }
    }
}