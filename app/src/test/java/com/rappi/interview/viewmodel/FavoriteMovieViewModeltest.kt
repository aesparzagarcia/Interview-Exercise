package com.rappi.interview.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rappi.interview.abstraction.state.FavoriteState
import com.rappi.interview.abstraction.state.LoaderState
import com.rappi.interview.abstraction.util.rx.TestSchedulerProvider
import com.rappi.interview.data.domain.MovieUseCase
import com.rappi.interview.data.entity.Movie
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

class FavoriteMovieViewModeltest {
    /* rule executor */
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    /* prepare for viewmodel (including class dependencies) */
    @Mock
    lateinit var useCase: MovieUseCase
    private lateinit var viewModel: FavoriteMovieViewModel

    /* observable and captor */
    @Mock
    lateinit var favoriteMoviesObservable: Observer<List<Movie>>
    @Mock
    lateinit var favoriteStateObservabele: Observer<FavoriteState>
    @Mock
    lateinit var stateObservable: Observer<LoaderState>

    @Captor
    lateinit var favoriteMovieCaptor: ArgumentCaptor<List<Movie>>
    @Captor
    lateinit var favoriteStateCaptor: ArgumentCaptor<FavoriteState>
    @Captor
    lateinit var stateCaptor: ArgumentCaptor<LoaderState>

    /* usecase response mock */
    private val movieReturnValue = Flowable.just(moviesData)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val scheduler = TestSchedulerProvider()
        viewModel = FavoriteMovieViewModel(useCase, scheduler)

        viewModel.favoriteMovies.observeForever(favoriteMoviesObservable)
        viewModel.favoriteState.observeForever(favoriteStateObservabele)
        viewModel.state.observeForever(stateObservable)
    }

    @Test
    fun `it should return response favorite movie correctly`() {
        Mockito.`when`(useCase.getAllFavoriteMovies()).thenReturn(movieReturnValue)
        viewModel.getAllFavoriteMovie()
        Mockito.verify(favoriteMoviesObservable, Mockito.atLeastOnce())
            .onChanged(favoriteMovieCaptor.capture())
        movieReturnValue
            .test()
            .assertValue {
                it.first() == favoriteMovieCaptor.value.first()
            }
    }

    @Test
    fun `it should return empty favorite movie`() {
        val returnEmptyValue = Flowable.just(listOf<Movie>())
        Mockito.`when`(useCase.getAllFavoriteMovies()).thenReturn(returnEmptyValue)
        viewModel.getAllFavoriteMovie()
        Mockito.verify(favoriteMoviesObservable, Mockito.atLeastOnce())
            .onChanged(favoriteMovieCaptor.capture())
        assert(favoriteMovieCaptor.value.isEmpty())
    }

    @Test
    fun `state handling correctly`() {
        // handling state popular movie
        Mockito.`when`(useCase.getAllFavoriteMovies()).thenReturn(movieReturnValue)
        viewModel.getAllFavoriteMovie()
        Mockito.verify(stateObservable, Mockito.atLeastOnce()).onChanged(stateCaptor.capture())
        assert(LoaderState.ShowLoading == stateCaptor.allValues[0]) //first, loader is showing
        assert(LoaderState.HideLoading == stateCaptor.allValues[1]) //then, hide the loader
    }

    @Test
    fun `favorite state handling correctly when insert favorite movie`() {
        Mockito.`when`(useCase.insertFavoritesMovie(movieData)).thenReturn(Completable.complete())
        // When updating the user name
        viewModel.insertFavoriteMovie(movieData)
        Mockito.verify(favoriteStateObservabele, Mockito.atLeastOnce())
            .onChanged(favoriteStateCaptor.capture())
        assert(FavoriteState.InsertSuccess == favoriteStateCaptor.allValues[0]) //insert success
    }

    @Test
    fun `favorite state handling correctly when delete favorite movie`() {
        Mockito.`when`(useCase.deleteFavoritesMovie(movieData)).thenReturn(Completable.complete())
        // When updating the user name
        viewModel.deleteFavoriteMovie(movieData)
        Mockito.verify(favoriteStateObservabele, Mockito.atLeastOnce())
            .onChanged(favoriteStateCaptor.capture())
        assert(FavoriteState.DeleteSuccess == favoriteStateCaptor.allValues[0]) //delete success
    }

    @After
    fun tearDown() {
        Mockito.clearInvocations(useCase, favoriteMoviesObservable)
        Mockito.clearInvocations(useCase, favoriteStateObservabele)
        Mockito.clearInvocations(useCase, stateObservable)
    }

    companion object {
        private val movieData = Movie(
            0.0,
            0,
            false,
            "posterPath",
            0,
            false,
            "backdropPath",
            "originalLanguage",
            "originalTitle",
            listOf(),
            "title",
            0.0,
            "overview",
            "releaseDate",
            listOf()
        )
        private val moviesData = listOf(
            movieData
        )
    }
}
