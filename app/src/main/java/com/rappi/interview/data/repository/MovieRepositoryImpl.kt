package com.rappi.interview.data.repository

import com.rappi.interview.data.entity.Movie
import com.rappi.interview.data.entity.MovieResponse
import com.rappi.interview.data.entity.ReviewResponse
import com.rappi.interview.data.presistance.FavoriteMovieDao
import com.rappi.interview.network.ApiService
import io.reactivex.Completable
import io.reactivex.Flowable
import okhttp3.ResponseBody
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val favoriteMovieDao: FavoriteMovieDao
) : MovieRepository {

    override fun getPopularMovie(): Flowable<MovieResponse> {
        return apiService.getPopularMovie()
    }

    override fun getTopRatedMovie(): Flowable<MovieResponse> {
        return apiService.getTopRatedMovie()
    }

    override fun getNowPlayingMovie(): Flowable<MovieResponse> {
        return apiService.getNowPlayingMovie()
    }

    override fun getUpcomingMovie(): Flowable<MovieResponse> {
        return apiService.getUpcomingMovie()
    }

    override fun getDetailMovie(idMovie: Int): Flowable<Movie> {
        return apiService.getDetailsMovie(idMovie)
    }

    override fun getAllFavoriteMovies(): Flowable<List<Movie>> {
        return favoriteMovieDao.getAllFavoriteMovies()
    }

    override fun deleteFavoriteMovie(movie: Movie): Completable {
        return favoriteMovieDao.deleteFavoriteMovie(movie)
    }

    override fun insertFavoriteMovie(movie: Movie): Completable {
        return favoriteMovieDao.insertFavoriteMovie(movie)
    }

    override fun getAllReviewMovie(idMovie: Int): Flowable<ReviewResponse> {
        return apiService.getReviewsMovie(idMovie)
    }

    override fun getSimilarMovie(idMoview: Int): Flowable<ResponseBody> {
        return apiService.getSimilarMovies(idMoview)
    }

}