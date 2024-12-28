package com.example.walkwell.di

import android.content.Context
import androidx.room.Room
import com.example.walkwell.data.local.TrackingDatabase
import com.example.walkwell.data.local.dao.TrackingDao
import com.example.walkwell.data.repository.TrackingRepositoryImpl
import com.example.walkwell.domain.repository.TrackingRepository
import com.example.walkwell.domain.usecase.DeleteTrackingUseCase
import com.example.walkwell.domain.usecase.GetAllTrackingSortedByAvgSpeedUseCase
import com.example.walkwell.domain.usecase.GetAllTrackingSortedByCalorieRunUseCase
import com.example.walkwell.domain.usecase.GetAllTrackingSortedByDateUseCase
import com.example.walkwell.domain.usecase.GetAllTrackingSortedByDistanceUseCase
import com.example.walkwell.domain.usecase.GetTotalAvgSpeedUseCase
import com.example.walkwell.domain.usecase.GetTotalCaloriesBurnedUseCase
import com.example.walkwell.domain.usecase.GetTotalDistanceUseCase
import com.example.walkwell.domain.usecase.GetTotalTimeInMillisUseCase
import com.example.walkwell.domain.usecase.InsertTrackingUseCase
import com.example.walkwell.domain.usecase.TrackingUseCase
import com.example.walkwell.utilities.ComponentTags.Companion.TRACKING_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context,
    ) = Room.databaseBuilder(
        app, TrackingDatabase::class.java, TRACKING_DATABASE
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun trackingDao(db: TrackingDatabase) = db.getTrackingDao()

    @Singleton
    @Provides
    fun providesTrackingRepository(trackingDao: TrackingDao) = TrackingRepositoryImpl(trackingDao)

    @Singleton
    @Provides
    fun providesTrackingUseCase(trackingRepository: TrackingRepository): TrackingUseCase {
        return TrackingUseCase(
            insertTrackingUseCase = InsertTrackingUseCase(trackingRepository),
            deleteTrackingUseCase = DeleteTrackingUseCase(trackingRepository),
            getAllTrackingSortedByDateUseCase = GetAllTrackingSortedByDateUseCase(trackingRepository),
            getAllTrackingSortedByAvgSpeedUseCase = GetAllTrackingSortedByAvgSpeedUseCase(
                trackingRepository
            ),
            getAllTrackingSortedByCalorieRunUseCase = GetAllTrackingSortedByCalorieRunUseCase(
                trackingRepository
            ),
            getAllTrackingSortedByDistanceUseCase = GetAllTrackingSortedByDistanceUseCase(
                trackingRepository
            ),
            getTotalDistanceUseCase = GetTotalDistanceUseCase(trackingRepository),
            getTotalAvgSpeedUseCase = GetTotalAvgSpeedUseCase(trackingRepository),
            getTotalCaloriesBurnedUseCase = GetTotalCaloriesBurnedUseCase(trackingRepository),
            getTotalTimeInMillisUseCase = GetTotalTimeInMillisUseCase(trackingRepository)
        )
    }
}