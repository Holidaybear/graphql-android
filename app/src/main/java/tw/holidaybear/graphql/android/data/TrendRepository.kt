package tw.holidaybear.graphql.android.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import tw.holidaybear.graphql.android.TrendQuery
import tw.holidaybear.graphql.android.type.SearchType

class TrendRepository(private val apolloClient: ApolloClient) {

    fun getTrends(): Observable<List<Repo>> {

        val trendQuery = TrendQuery.builder()
                .first(25)
                .query("created:>2018-07-15 sort:stars-desc")
                .type(SearchType.REPOSITORY)
                .build()

        return Rx2Apollo.from(apolloClient.query(trendQuery))
                .map { response -> RepoMapper.toRepos(response.data()) }
    }
}