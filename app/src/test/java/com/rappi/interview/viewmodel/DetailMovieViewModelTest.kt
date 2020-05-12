package com.rappi.interview.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rappi.interview.abstraction.state.LoaderState
import com.rappi.interview.abstraction.util.rx.TestSchedulerProvider
import com.rappi.interview.data.domain.MovieUseCase
import com.rappi.interview.data.entity.Movie
import com.rappi.interview.data.entity.Review
import com.rappi.interview.data.entity.ReviewResponse
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

class DetailMovieViewModelTest {

    /* rule executor */
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    /* prepare for viewmodel (including class dependencies) */
    @Mock
    lateinit var useCase: MovieUseCase
    private lateinit var viewModel: DetailMovieViewModel

    /* observable and captor */
    @Mock
    lateinit var detailMovieObservable: Observer<Movie>
    @Mock
    lateinit var reviewsObservabel: Observer<ReviewResponse>
    @Mock
    lateinit var stateObservable: Observer<LoaderState>

    @Captor
    lateinit var detailMovieCaptor: ArgumentCaptor<Movie>
    @Captor
    lateinit var reviewsMovieCaptor: ArgumentCaptor<ReviewResponse>
    @Captor
    lateinit var stateCaptor: ArgumentCaptor<LoaderState>

    /* usecase response mock */
    private val movieReturnValue = Flowable.just(movieData)
    private val reviewsReturnValue = Flowable.just(reviewsData)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val scheduler = TestSchedulerProvider()
        viewModel = DetailMovieViewModel(useCase, scheduler)

        viewModel.detailMovie.observeForever(detailMovieObservable)
        viewModel.reviews.observeForever(reviewsObservabel)
        viewModel.state.observeForever(stateObservable)
    }

    @Test
    fun `it should return response review movie correctly`() {
        Mockito.`when`(useCase.getReviewMovies(1)).thenReturn(reviewsReturnValue)
        viewModel.getReviewMovie(1)
        Mockito.verify(reviewsObservabel, Mockito.atLeastOnce())
            .onChanged(reviewsMovieCaptor.capture())
        reviewsReturnValue
            .test()
            .assertValue {
                it.results.first() == reviewsMovieCaptor.value.results.first()
            }
    }

    @Test
    fun `it should return empty review movie`() {
        val returnEmptyValue = Flowable.just(ReviewResponse(0, 0, listOf(), 0))
        Mockito.`when`(useCase.getReviewMovies(1)).thenReturn(returnEmptyValue)
        viewModel.getReviewMovie(1)
        Mockito.verify(reviewsObservabel, Mockito.atLeastOnce())
            .onChanged(reviewsMovieCaptor.capture())
        assert(reviewsMovieCaptor.value.results.isEmpty())
    }

    @Test
    fun `it should return detail movie correctly`() {
        Mockito.`when`(useCase.getDetailMovie(1)).thenReturn(movieReturnValue)
        viewModel.getDetailMovie(1)
        Mockito.verify(detailMovieObservable, Mockito.atLeastOnce())
            .onChanged(detailMovieCaptor.capture())
        movieReturnValue
            .test()
            .assertValue {
                it == detailMovieCaptor.value
            }
    }

    @Test
    fun `it should return empty detail movie`() {
        val returnEmptyValue = Flowable.just(Movie())
        Mockito.`when`(useCase.getDetailMovie(1)).thenReturn(returnEmptyValue)
        viewModel.getDetailMovie(1)
        Mockito.verify(detailMovieObservable, Mockito.atLeastOnce())
            .onChanged(detailMovieCaptor.capture())
        assert(detailMovieCaptor.value == Movie())
    }

    @Test
    fun `state handling correctly`() {
        // handling state popular movie
        Mockito.`when`(useCase.getDetailMovie(1)).thenReturn(movieReturnValue)
        viewModel.getDetailMovie(1)
        Mockito.verify(stateObservable, Mockito.atLeastOnce()).onChanged(stateCaptor.capture())
        assert(LoaderState.ShowLoading == stateCaptor.allValues[0]) //first, loader is showing
        assert(LoaderState.HideLoading == stateCaptor.allValues[1]) //then, hide the loader

        // handling state upcoming movie
        Mockito.`when`(useCase.getReviewMovies(1)).thenReturn(reviewsReturnValue)
        viewModel.getReviewMovie(1)
        Mockito.verify(stateObservable, Mockito.atLeastOnce()).onChanged(stateCaptor.capture())
        assert(LoaderState.ShowLoading == stateCaptor.allValues[0]) //first, loader is showing
        assert(LoaderState.HideLoading == stateCaptor.allValues[1]) //then, hide the loader
    }

    @After
    fun tearDown() {
        Mockito.clearInvocations(useCase, detailMovieObservable)
        Mockito.clearInvocations(useCase, reviewsObservabel)
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

        private val review = Review(
            "id",
            "author",
            "content",
            "url"
        )

        private val reviewsData = ReviewResponse(
            1,
            1,
            listOf(review),
            1,
            10
        )
    }
}