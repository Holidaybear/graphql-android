package tw.holidaybear.graphql.android.repos

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import tw.holidaybear.graphql.android.data.Repo
import tw.holidaybear.graphql.android.data.TrendRepository

class MainViewModel(private val repository: TrendRepository) : ViewModel() {

    fun getTrends(): Observable<List<Repo>> {
        return repository.getTrends()
    }
}