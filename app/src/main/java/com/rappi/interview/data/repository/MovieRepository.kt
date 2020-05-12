package com.rappi.interview.data.repository

import com.rappi.interview.data.entity.Movie
import com.rappi.interview.data.entity.MovieResponse
import com.rappi.interview.data.entity.ReviewResponse
import io.reactivex.Completable
import io.reactivex.Flowable
import okhttp3.ResponseBody

interface MovieRepository {
    fun getPopularMovie(): Flowable<MovieResponse>
    fun getTopRatedMovie(): Flowable<MovieResponse>
    fun getNowPlayingMovie(): Flowable<MovieResponse>
    fun getUpcomingMovie(): Flowable<MovieResponse>
    fun getDetailMovie(idMovie: Int): Flowable<Movie>
    fun getAllFavoriteMovies(): Flowable<List<Movie>>
    fun deleteFavoriteMovie(movie: Movie): Completable
    fun insertFavoriteMovie(movie: Movie): Completable
    fun getAllReviewMovie(idMovie: Int): Flowable<ReviewResponse>
    fun getSimilarMovie(idMoview: Int): Flowable<ResponseBody>

}
