package tw.holidaybear.graphql.android.data

import tw.holidaybear.graphql.android.TrendQuery

object RepoMapper {

    fun toRepos(data: TrendQuery.Data?): List<Repo> {
        val repoList = mutableListOf<Repo>()

        for (repoEdge in data?.search()?.edges()!!) {
            val repoNode = repoEdge.node() as TrendQuery.AsRepository
            repoList.add(toRepo(repoNode))
        }

        return repoList
    }

    fun toRepo(node: TrendQuery.AsRepository): Repo {
        return Repo(node.id(),
                node.name(),
                node.owner().login(),
                node.description(),
                node.stargazers().totalCount(),
                node.primaryLanguage()?.name(),
                node.primaryLanguage()?.color(),
                node.licenseInfo()?.name())
    }
}