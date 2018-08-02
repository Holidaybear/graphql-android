package tw.holidaybear.graphql.android.data

data class Repo(
        val id: String,
        val name: String,
        val owner: String,
        val description: String?,
        val starCount: Int,
        val language: String?,
        val languageColor: String?,
        val licenseInfo: String?)