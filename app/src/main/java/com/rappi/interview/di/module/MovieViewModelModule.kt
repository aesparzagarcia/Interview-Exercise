package com.rappi.interview.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rappi.interview.abstraction.util.viewmodel.ViewModelFactory
import com.rappi.interview.abstraction.util.viewmodel.ViewModelKey
import com.rappi.interview.di.MovieScope
import com.rappi.interview.viewmodel.DetailMovieViewModel
import com.rappi.interview.viewmodel.FavoriteMovieViewModel
import com.rappi.interview.viewmodel.MovieViewModel
import com.rappi.interview.viewmodel.SimilarMovieViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MovieViewModelModule {

    @MovieScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    internal abstract fun bindMovieViewModel(viewModel: MovieViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailMovieViewModel::class)
    internal abstract fun bindDetailMovieViewModel(viewModel: DetailMovieViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteMovieViewModel::class)
    internal abstract fun bindFavoriteMovieViewModel(viewModel: FavoriteMovieViewModel): ViewModel
}