package com.example.trippack.di

import com.example.trippack.data.local.db.DestinationDao
import com.example.trippack.data.local.db.PackingItemDao
import com.example.trippack.data.local.db.TripDao
import com.example.trippack.data.local.db.WeatherCacheDao
import com.example.trippack.data.remote.api.GeocodingApiService
import com.example.trippack.data.remote.api.WeatherApiService
import com.example.trippack.data.repository.AuthRepositoryImpl
import com.example.trippack.data.repository.DestinationRepositoryImpl
import com.example.trippack.data.repository.PackingItemRepositoryImpl
import com.example.trippack.data.repository.TripRepositoryImpl
import com.example.trippack.data.repository.WeatherRepositoryImpl
import com.example.trippack.domain.repository.AuthRepository
import com.example.trippack.domain.repository.DestinationRepository
import com.example.trippack.domain.repository.PackingItemRepository
import com.example.trippack.domain.repository.TripRepository
import com.example.trippack.domain.repository.WeatherRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideDestinationRepository(dao: DestinationDao): DestinationRepository {
        return DestinationRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideTripRepository(dao: TripDao): TripRepository {
        return TripRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(dao: WeatherCacheDao, weatherApi: WeatherApiService, geocodingApi: GeocodingApiService): WeatherRepository {
        return WeatherRepositoryImpl(dao, weatherApi, geocodingApi)
    }

    @Provides
    @Singleton
    fun providePackingItemRepository(dao: PackingItemDao): PackingItemRepository {
        return PackingItemRepositoryImpl(dao)
    }
}