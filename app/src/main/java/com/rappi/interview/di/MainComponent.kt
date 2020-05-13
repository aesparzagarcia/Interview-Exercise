package com.rappi.interview.di

import com.rappi.interview.ui.main.MainActivity
import com.rappi.interview.di.module.MovieModule
import com.rappi.interview.di.module.MovieViewModelModule
import com.rappi.interview.di.module.RoomModule
import com.rappi.interview.ui.detail.DetailMovieActivity
import com.rappi.interview.ui.favorite.FavoriteMovieActivity
import com.rappi.interview.ui.similar.SimilarMovieActivity
import dagger.Component


@MovieScope
@Component(
    modules = [
        MovieModule::class,
        MovieViewModelModule::class,
        RoomModule::class
    ]
)
interface MainComponent {
    fun injectMain(activity: MainActivity)
    fun injectDetail(activity: DetailMovieActivity)
    fun injectFavorite(activity: FavoriteMovieActivity)
}