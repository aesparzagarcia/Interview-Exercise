package com.rappi.interview.di.module

import com.rappi.interview.abstraction.util.rx.AppSchedulerProvider
import com.rappi.interview.abstraction.util.rx.SchedulerProvider
import com.rappi.interview.data.domain.MovieUseCase
import com.rappi.interview.data.presistance.FavoriteMovieDao
import com.rappi.interview.data.repository.MovieRepository
import com.rappi.interview.data.repository.MovieRepositoryImpl
import com.rappi.interview.di.MovieScope
import com.rappi.interview.network.ApiService
import com.rappi.interview.network.Network
import dagger.Module
import dagger.Provides
import retrofit2.create

@Module
class MovieModule {

    @Provides
    @MovieScope
    fun provideNetworkBuilder(): ApiService {
        return Network.builder().create()
    }

    @Provides
    @MovieScope
    fun provideMovieRepository(
        service: ApiService,
        favoriteMovieDao: FavoriteMovieDao
    ): MovieRepository {
        return MovieRepositoryImpl(service, favoriteMovieDao)
    }

    @Provides
    @MovieScope
    fun provideMovieUseCase(repository: MovieRepository): MovieUseCase {
        return MovieUseCase(repository)
    }

    @Provides
    @MovieScope
    fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }
}