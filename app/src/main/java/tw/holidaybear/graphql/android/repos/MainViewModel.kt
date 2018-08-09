package tw.holidaybear.graphql.android.repos

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import io.reactivex.Single
import tw.holidaybear.graphql.android.data.Repo
import tw.holidaybear.graphql.android.data.TrendRepository

class MainViewModel(private val repository: TrendRepository) : ViewModel() {

    val loading = ObservableBoolean(false)

    fun getRepos(): Single<List<Repo>> {
        return repository.getRepos()
                .doOnSubscribe { startLoad() }
                .doAfterTerminate { stopLoad() }
    }

    private fun startLoad() = loading.set(true)

    private fun stopLoad() = loading.set(false)
}