package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.scudderapps.moviesup.models.main.TV
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.tv.tvlist.TvPagedListRepository
import io.reactivex.disposables.CompositeDisposable

class TvListViewModel(
    private val tvRepository: TvPagedListRepository,
    private val type: String
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val tvPagedList: LiveData<PagedList<TV>> by lazy {
        tvRepository.fetchingMovieList(compositeDisposable, type)
    }

    val networkState: LiveData<NetworkState> by lazy {
        tvRepository.getNetworkState()
    }


    fun movieListIsEmpty(): Boolean {
        return tvPagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}