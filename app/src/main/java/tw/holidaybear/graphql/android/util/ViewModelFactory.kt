package tw.holidaybear.graphql.android.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import tw.holidaybear.graphql.android.data.TrendRepository
import tw.holidaybear.graphql.android.repos.MainViewModel

class ViewModelFactory(private val repository: TrendRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}