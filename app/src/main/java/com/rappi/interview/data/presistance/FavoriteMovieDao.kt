package com.rappi.interview.data.presistance

import androidx.room.*
import com.rappi.interview.data.entity.Movie
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteMovie(movie: Movie): Completable

    @Query("SELECT * FROM favorites_movie")
    fun getAllFavoriteMovies(): Flowable<List<Movie>>

    @Delete
    fun deleteFavoriteMovie(movie: Movie): Completable
}