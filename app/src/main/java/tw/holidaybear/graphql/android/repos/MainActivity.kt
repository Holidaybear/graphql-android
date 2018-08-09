package tw.holidaybear.graphql.android.repos

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tw.holidaybear.graphql.android.R
import tw.holidaybear.graphql.android.databinding.ActivityMainBinding
import tw.holidaybear.graphql.android.util.Injection
import tw.holidaybear.graphql.android.util.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ReposAdapter
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModelFactory = Injection.provideViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        adapter = ReposAdapter(mutableListOf())

        setSupportActionBar(binding.toolbar)
        binding.recyclerview.adapter = adapter
        getRepos()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun getRepos() {
        disposables.add(viewModel.getTrends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { binding.progress.visibility = View.VISIBLE }
                .subscribe {
                    binding.progress.visibility = View.GONE
                    adapter.swapItems(it) })
    }
}
