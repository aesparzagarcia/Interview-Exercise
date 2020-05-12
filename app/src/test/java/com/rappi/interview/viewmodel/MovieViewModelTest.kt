package com.rappi.interview.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rappi.interview.abstraction.state.LoaderState
import com.rappi.interview.abstraction.util.rx.TestSchedulerProvider
import com.rappi.interview.data.domain.MovieUseCase
import com.rappi.interview.data.entity.Movie
import com.rappi.interview.data.entity.MovieResponse
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

class MovieViewModelTest {

    /* rule executor */
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    /* prepare for viewmodel (including class dependencies) */
    @Mock
    lateinit var useCase: MovieUseCase
    private lateinit var viewModel: MovieViewModel

    /* observable and captor */
    @Mock
    lateinit var moviesObservable: Observer<MovieResponse>
    @Mock
    lateinit var stateObservable: Observer<LoaderState>

    @Captor
    lateinit var moviesCaptor: ArgumentCaptor<MovieResponse>
    @Captor
    lateinit var stateCaptor: ArgumentCaptor<LoaderState>

    /* usecase response mock */
    private val returnValue = Flowable.just(moviesData)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        //init viewmodel
        val scheduler = TestSchedulerProvider()
        viewModel = MovieViewModel(useCase, scheduler)

        //observe
        viewModel.movies.observeForever(moviesObservable)
        viewModel.state.observeForever(stateObservable)

    }

    @Test
    fun `it should return response popular movie correctly`() {
        Mockito.`when`(useCase.getPopularMovie()).thenReturn(returnValue)
        viewModel.getPopularMovie()
        Mockito.verify(moviesObservable, Mockito.atLeastOnce()).onChanged(moviesCaptor.capture())
        returnValue
            .test()
            .assertValue {
                it.results.first() == moviesCaptor.value.results.first()
            }
    }

    @Test
    fun `it should return response upcoming movie correctly`() {
        Mockito.`when`(useCase.getUpcomingMovie()).thenReturn(returnValue)
        viewModel.getUpcomingMovie()
        Mockito.verify(moviesObservable, Mockito.atLeastOnce()).onChanged(moviesCaptor.capture())
        returnValue
            .test()
            .assertValue {
                it.results.first() == moviesCaptor.value.results.first()
            }
    }

    @Test
    fun `it should return response top rated movie correctly`() {
        Mockito.`when`(useCase.getTopRatedMovie()).thenReturn(returnValue)
        viewModel.getTopRatedMovie()
        Mockito.verify(moviesObservable, Mockito.atLeastOnce()).onChanged(moviesCaptor.capture())
        returnValue
            .test()
            .assertValue {
                it.results.first() == moviesCaptor.value.results.first()
            }
    }

    @Test
    fun `it should return response now playing movie correctly`() {
        Mockito.`when`(useCase.getNowPlayingMovie()).thenReturn(returnValue)
        viewModel.getNowPlayingMovie()
        Mockito.verify(moviesObservable, Mockito.atLeastOnce()).onChanged(moviesCaptor.capture())
        returnValue
            .test()
            .assertValue {
                it.results.first() == moviesCaptor.value.results.first()
            }
    }

    @Test
    fun `it should return empty popular movie`() {
        val returnEmptyValue = Flowable.just(MovieResponse(0, 0, 0, listOf()))
        Mockito.`when`(useCase.getPopularMovie()).thenReturn(returnEmptyValue)
        viewModel.getPopularMovie()
        Mockito.verify(moviesObservable, Mockito.atLeastOnce()).onChanged(moviesCaptor.capture())
        assert(moviesCaptor.value.results.isEmpty())
    }

    @Test
    fun `it should return empty upcoming movie`() {
        val returnEmptyValue = Flowable.just(MovieResponse(0, 0, 0, listOf()))
        Mockito.`when`(useCase.getUpcomingMovie()).thenReturn(returnEmptyValue)
        viewModel.getUpcomingMovie()
        Mockito.verify(moviesObservable, Mockito.atLeastOnce()).onChanged(moviesCaptor.capture())
        assert(moviesCaptor.value.results.isEmpty())
    }

    @Test
    fun `it should return empty top rated movie`() {
        val returnEmptyValue = Flowable.just(MovieResponse(0, 0, 0, listOf()))
        Mockito.`when`(useCase.getTopRatedMovie()).thenReturn(returnEmptyValue)
        viewModel.getTopRatedMovie()
        Mockito.verify(moviesObservable, Mockito.atLeastOnce()).onChanged(moviesCaptor.capture())
        assert(moviesCaptor.value.results.isEmpty())
    }

    @Test
    fun `it should return empty now playing movie`() {
        val returnEmptyValue = Flowable.just(MovieResponse(0, 0, 0, listOf()))
        Mockito.`when`(useCase.getNowPlayingMovie()).thenReturn(returnEmptyValue)
        viewModel.getNowPlayingMovie()
        Mockito.verify(moviesObservable, Mockito.atLeastOnce()).onChanged(moviesCaptor.capture())
        assert(moviesCaptor.value.results.isEmpty())
    }

    @Test
    fun `state handling correctly`() {
        // handling state popular movie
        Mockito.`when`(useCase.getPopularMovie()).thenReturn(returnValue)
        viewModel.getPopularMovie()
        Mockito.verify(stateObservable, Mockito.atLeastOnce()).onChanged(stateCaptor.capture())
        assert(LoaderState.ShowLoading == stateCaptor.allValues[0]) //first, loader is showing
        assert(LoaderState.HideLoading == stateCaptor.allValues[1]) //then, hide the loader

        // handling state upcoming movie
        Mockito.`when`(useCase.getUpcomingMovie()).thenReturn(returnValue)
        viewModel.getUpcomingMovie()
        Mockito.verify(stateObservable, Mockito.atLeastOnce()).onChanged(stateCaptor.capture())
        assert(LoaderState.ShowLoading == stateCaptor.allValues[0]) //first, loader is showing
        assert(LoaderState.HideLoading == stateCaptor.allValues[1]) //then, hide the loader

        // handling state top rated movie
        Mockito.`when`(useCase.getTopRatedMovie()).thenReturn(returnValue)
        viewModel.getTopRatedMovie()
        Mockito.verify(stateObservable, Mockito.atLeastOnce()).onChanged(stateCaptor.capture())
        assert(LoaderState.ShowLoading == stateCaptor.allValues[0]) //first, loader is showing
        assert(LoaderState.HideLoading == stateCaptor.allValues[1]) //then, hide the loader

        // handling state now playing movie
        Mockito.`when`(useCase.getNowPlayingMovie()).thenReturn(returnValue)
        viewModel.getNowPlayingMovie()
        Mockito.verify(stateObservable, Mockito.atLeastOnce()).onChanged(stateCaptor.capture())
        assert(LoaderState.ShowLoading == stateCaptor.allValues[0]) //first, loader is showing
        assert(LoaderState.HideLoading == stateCaptor.allValues[1]) //then, hide the loader
    }

    @After
    fun tearDown() {
        Mockito.clearInvocations(useCase, moviesObservable)
        Mockito.clearInvocations(useCase, stateObservable)

    }

    /* mock data */
    companion object {
        private val movies = listOf(
            Movie(
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
        )

        private val moviesData = MovieResponse(
            1,
            1,
            10,
            movies
        )
    }
}