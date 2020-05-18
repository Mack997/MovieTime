package com.scudderapps.moviesup.repository.movie.trending

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.movie.Movie
import io.reactivex.disposables.CompositeDisposable

class TrendingDataSourceFactory(
    private val apiService: ApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val type: String
) : DataSource.Factory<Int, Movie>() {

    val discoverLiveDataSource = MutableLiveData<TrendingDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val discoverDataSource =
            TrendingDataSource(
                apiService,
                compositeDisposable,
                type
            )

        discoverLiveDataSource.postValue(discoverDataSource)
        return discoverDataSource
    }
}