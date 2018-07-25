package tw.holidaybear.graphql.android

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable

class MainViewModel : ViewModel() {

    private val repository = TrendRepository()

    fun getTrends(): Observable<List<Repo>> {
        return repository.getTrends()
    }
}