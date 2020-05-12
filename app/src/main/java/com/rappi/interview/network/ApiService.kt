package com.rappi.interview.network

import com.rappi.interview.data.entity.Movie
import com.rappi.interview.data.entity.MovieResponse
import com.rappi.interview.data.entity.ReviewResponse
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("movie/popular")
    fun getPopularMovie(): Flowable<MovieResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovie(): Flowable<MovieResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(): Flowable<MovieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovie(): Flowable<MovieResponse>

    @GET("movie/{movie_id}")
    fun getDetailsMovie(
        @Path("movie_id") movieId: Int
    ): Flowable<Movie>

    @GET("movie/{movie_id}/reviews")
    fun getReviewsMovie(
        @Path("movie_id") movieId: Int
    ): Flowable<ReviewResponse>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(
        @Path("movie_id") movieId: Int
    ): Flowable<ResponseBody>

}