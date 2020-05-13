package com.rappi.interview.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rappi.interview.abstraction.base.BaseViewModel
import com.rappi.interview.abstraction.state.LoaderState
import com.rappi.interview.abstraction.util.rx.SchedulerProvider
import com.rappi.interview.data.domain.MovieUseCase
import okhttp3.ResponseBody
import javax.inject.Inject

interface SimilarMovieContract {
    fun onErrorGetSimilarMovie(throwable: Throwable)
    fun getSimilarMoview(idMovie: Int)
}

class SimilarMovieViewModel @Inject constructor(
    private val useCase: MovieUseCase,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(), SimilarMovieContract {

    private val _state = MutableLiveData<LoaderState>()
    val state: LiveData<LoaderState>
        get() = _state

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    override fun onErrorGetSimilarMovie(throwable: Throwable) {
        _error.postValue(throwable.message)
        hideLoading()
    }

    override fun getSimilarMoview(idMovie: Int) {

    }

    private fun showLoading() {
        _state.postValue(LoaderState.ShowLoading)
    }

    private fun hideLoading() {
        _state.postValue(LoaderState.HideLoading)
    }
}