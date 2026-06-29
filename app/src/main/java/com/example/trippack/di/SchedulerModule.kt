package com.example.trippack.di

import com.example.trippack.data.scheduler.TripSchedulerImpl
import com.example.trippack.domain.scheduler.TripScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulerModule {
    @Binds
    @Singleton
    abstract fun bindTripScheduler(impl: TripSchedulerImpl): TripScheduler
}