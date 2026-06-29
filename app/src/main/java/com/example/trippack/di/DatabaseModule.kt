package com.example.trippack.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trippack.data.local.db.DestinationDao
import com.example.trippack.data.local.db.PackingItemDao
import com.example.trippack.data.local.db.TripDao
import com.example.trippack.data.local.db.TripPackDatabase
import com.example.trippack.data.local.db.WeatherCacheDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    val MIGRATION_1_2 = object : Migration(1, 2){
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE trips ADD COLUMN travelerCount INTEGER NOT NULL DEFAULT 1")
            db.execSQL("ALTER TABLE trips ADD COLUMN estimatedBudget REAL NOT NULL DEFAULT 0.0")
        }
    }
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TripPackDatabase {
        return Room.databaseBuilder(
            context,
            TripPackDatabase::class.java,
            "trippack_database"
        ).addMigrations(MIGRATION_1_2).build()
    }

    @Provides
    fun provideDestinationDao(database: TripPackDatabase): DestinationDao = database.destinationDao()

    @Provides
    fun provideTripDao(database: TripPackDatabase): TripDao = database.tripDao()

    @Provides
    fun providePackingItemDao(database: TripPackDatabase): PackingItemDao = database.packingItemDao()

    @Provides
    fun provideWeatherCacheDao(database: TripPackDatabase): WeatherCacheDao = database.weatherCacheDao()
}