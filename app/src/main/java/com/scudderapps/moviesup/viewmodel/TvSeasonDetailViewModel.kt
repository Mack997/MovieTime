package com.scudderapps.moviesup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.scudderapps.moviesup.models.common.CastResponse
import com.scudderapps.moviesup.models.common.VideoResponse
import com.scudderapps.moviesup.models.tv.TvSeasonDetails
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.tv.seasons.SeasonDetailRepository
import io.reactivex.disposables.CompositeDisposable

class TvSeasonDetailViewModel(
    private val seasonDetailRepository: SeasonDetailRepository,
    tvId: Int,
    seasonNumber: Int
) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val tvSeasonDetails: LiveData<TvSeasonDetails> by lazy {
        seasonDetailRepository.fetchingTvSeasonDetails(compositeDisposable, tvId, seasonNumber)
    }

    val tvSeasonVideos: LiveData<VideoResponse> by lazy {
        seasonDetailRepository.fetchingTvSeasonVideos(compositeDisposable, tvId, seasonNumber)
    }

    val tvSeasonCast: LiveData<CastResponse> by lazy {
        seasonDetailRepository.fetchingTvSeasonCast(compositeDisposable, tvId, seasonNumber)
    }

    val networkState: LiveData<NetworkState> by lazy {
        seasonDetailRepository.getTvSeasonDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}