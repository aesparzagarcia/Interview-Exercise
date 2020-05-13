package com.rappi.interview.data.domain

import com.rappi.interview.abstraction.base.UseCase
import com.rappi.interview.data.entity.Movie
import com.rappi.interview.data.entity.MovieResponse
import com.rappi.interview.data.entity.ReviewResponse
import com.rappi.interview.data.repository.MovieRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import okhttp3.ResponseBody
import javax.inject.Inject

open class MovieUseCase @Inject constructor(
    private val repository: MovieRepository
) :  UseCase<Flowable<MovieResponse>>() {

    override fun getPopularMovie(): Flowable<MovieResponse> {
        return repository.getPopularMovie()
    }

    override fun getUpcomingMovie(): Flowable<MovieResponse> {
        return repository.getUpcomingMovie()
    }

    override fun getNowPlayingMovie(): Flowable<MovieResponse> {
        return repository.getNowPlayingMovie()
    }

    override fun getTopRatedMovie(): Flowable<MovieResponse> {
        return repository.getTopRatedMovie()
    }

    override fun getDetailMovie(idMovie: Int): Flowable<Movie> {
        return repository.getDetailMovie(idMovie)
    }

    override fun getAllFavoriteMovies(): Flowable<List<Movie>> {
        return repository.getAllFavoriteMovies()
    }

    override fun deleteFavoritesMovie(movie: Movie): Completable {
        return repository.deleteFavoriteMovie(movie)
    }

    override fun insertFavoritesMovie(movie: Movie): Completable {
        return repository.insertFavoriteMovie(movie)
    }

    override fun getReviewMovies(idMovie: Int): Flowable<ReviewResponse> {
        return repository.getAllReviewMovie(idMovie)
    }

    override fun getSimilarMovies(idMovie: Int): Flowable<ResponseBody> {
        return repository.getSimilarMovie(idMovie)
    }

}
