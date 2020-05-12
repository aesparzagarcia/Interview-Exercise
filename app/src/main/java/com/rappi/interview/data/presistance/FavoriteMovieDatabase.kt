package com.rappi.interview.data.presistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rappi.interview.data.entity.Movie

@Database(entities = [Movie::class], version = 1)
abstract class FavoriteMovieDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}