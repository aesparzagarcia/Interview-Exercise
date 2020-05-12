package com.rappi.interview.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rappi.interview.abstraction.base.BaseViewModel
import com.rappi.interview.abstraction.state.LoaderState
import com.rappi.interview.abstraction.util.rx.SchedulerProvider
import com.rappi.interview.data.domain.MovieUseCase
import com.rappi.interview.data.entity.Movie
import com.rappi.interview.data.entity.ReviewResponse
import javax.inject.Inject

interface DetailMovieContract {
    fun onErrorDetailMovie(throwable: Throwable)
    fun getDetailMovie(idMovie: Int)

    fun onErrorGetReviewMovie(throwable: Throwable)
    fun getReviewMovie(idMovie: Int)
}

class DetailMovieViewModel @Inject constructor(
    private val useCase: MovieUseCase,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(), DetailMovieContract {

    private val _detailMovie = MutableLiveData<Movie>()
    val detailMovie: LiveData<Movie>
        get() = _detailMovie

    private val _reviews = MutableLiveData<ReviewResponse>()
    val reviews: LiveData<ReviewResponse>
        get() = _reviews

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _state = MutableLiveData<LoaderState>()
    val state: LiveData<LoaderState>
        get() = _state

    override fun onErrorDetailMovie(throwable: Throwable) {
        _error.postValue(throwable.message)
        hideLoading()
    }

    override fun getDetailMovie(idMovie: Int) {
        subscribe(useCase.getDetailMovie(idMovie)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { showLoading() }
            .doOnError { onErrorDetailMovie(it) }
            .subscribe {
                hideLoading()
                _detailMovie.postValue(it)
            }
        )
    }

    override fun onErrorGetReviewMovie(throwable: Throwable) {
        _error.postValue(throwable.message)
        hideLoading()
    }

    override fun getReviewMovie(idMovie: Int) {
        subscribe(useCase.getReviewMovies(idMovie)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { showLoading() }
            .doOnError { onErrorGetReviewMovie(it) }
            .subscribe {
                hideLoading()
                _reviews.postValue(it)
            }
        )
    }

    private fun showLoading() {
        _state.postValue(LoaderState.ShowLoading)
    }

    private fun hideLoading() {
        _state.postValue(LoaderState.HideLoading)
    }

}