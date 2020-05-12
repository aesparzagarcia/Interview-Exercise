package com.rappi.interview.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rappi.interview.abstraction.base.BaseViewModel
import com.rappi.interview.abstraction.state.FavoriteState
import com.rappi.interview.abstraction.state.LoaderState
import com.rappi.interview.abstraction.util.rx.SchedulerProvider
import com.rappi.interview.data.domain.MovieUseCase
import com.rappi.interview.data.entity.Movie
import javax.inject.Inject

interface FavoriteMovieContract {

    fun onErrorGetAllFavoriteMovie(throwable: Throwable)
    fun getAllFavoriteMovie()

    fun onErrorDeleteFavoriteMovie(throwable: Throwable)
    fun deleteFavoriteMovie(movie: Movie)

    fun onErrorInsertFavoriteMovie(throwable: Throwable)
    fun insertFavoriteMovie(movie: Movie)

}

class FavoriteMovieViewModel @Inject constructor(
    private val useCase: MovieUseCase,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(), FavoriteMovieContract{

    private val _favoriteMovies = MutableLiveData<List<Movie>>()
    val favoriteMovies: LiveData<List<Movie>>
        get() = _favoriteMovies

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _state = MutableLiveData<LoaderState>()
    val state: LiveData<LoaderState>
        get() = _state

    private val _favoriteState = MutableLiveData<FavoriteState>()
    val favoriteState: LiveData<FavoriteState>
        get() = _favoriteState

    override fun onErrorGetAllFavoriteMovie(throwable: Throwable) {
        _error.postValue(throwable.message)
        hideLoading()
    }

    override fun getAllFavoriteMovie() {
        subscribe((useCase.getAllFavoriteMovies())
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { showLoading() }
            .doOnError { onErrorGetAllFavoriteMovie(it) }
            .subscribe {
                hideLoading()
                _favoriteMovies.postValue(it)
            }
        )
    }

    override fun onErrorDeleteFavoriteMovie(throwable: Throwable) {
        _error.postValue(throwable.message)
        hideLoading()
    }

    override fun deleteFavoriteMovie(movie: Movie) {
        subscribe((useCase.deleteFavoritesMovie(movie))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { showLoading() }
            .doOnError { onErrorDeleteFavoriteMovie(it) }
            .subscribe {
                hideLoading()
                deleteSuccess()
            }
        )
    }

    override fun onErrorInsertFavoriteMovie(throwable: Throwable) {
        _error.postValue(throwable.message)
        hideLoading()
    }

    override fun insertFavoriteMovie(movie: Movie) {
        subscribe((useCase.insertFavoritesMovie(movie))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { showLoading() }
            .doOnError { onErrorDeleteFavoriteMovie(it) }
            .subscribe {
                hideLoading()
                insertSuccess()
            }
        )
    }

    private fun insertSuccess() {
        _favoriteState.postValue(FavoriteState.InsertSuccess)
    }

    private fun deleteSuccess() {
        _favoriteState.postValue(FavoriteState.DeleteSuccess)
    }

    private fun showLoading() {
        _state.postValue(LoaderState.ShowLoading)
    }

    private fun hideLoading() {
        _state.postValue(LoaderState.HideLoading)
    }

}