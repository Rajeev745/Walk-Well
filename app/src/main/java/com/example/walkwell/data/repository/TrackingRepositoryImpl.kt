package com.example.walkwell.data.repository

import androidx.lifecycle.LiveData
import com.example.walkwell.data.local.dao.TrackingDao
import com.example.walkwell.data.local.entity.TrackingHistory
import com.example.walkwell.domain.repository.TrackingRepository

class TrackingRepositoryImpl(private val trackingDao: TrackingDao) : TrackingRepository {

    override suspend fun insertTracking(trackingHistory: TrackingHistory) {
        trackingDao.insertRun(trackingHistory)
    }

    override suspend fun deleteTracking(trackingHistory: TrackingHistory) {
        trackingDao.deleteRun(trackingHistory)
    }

    override fun getAllRunsSortedByDate(): LiveData<List<TrackingHistory>> {
        return trackingDao.getAllRunsSortedByDate()
    }

    override fun getAllRunsSortedByTimeInMillis(): LiveData<List<TrackingHistory>> {
        return trackingDao.getAllRunsSortedByTimeInMillis()
    }

    override fun getAllRunsSortedByCaloriesBurned(): LiveData<List<TrackingHistory>> {
        return trackingDao.getAllRunsSortedByCaloriesBurned()
    }

    override fun getAllRunsSortedByAvgSpeed(): LiveData<List<TrackingHistory>> {
        return trackingDao.getAllRunsSortedByAvgSpeed()
    }

    override fun getAllRunsSortedByDistance(): LiveData<List<TrackingHistory>> {
        return trackingDao.getAllRunsSortedByDistance()
    }

    override fun getTotalTimeInMillis(): LiveData<Long> {
        return trackingDao.getTotalTimeInMillis()
    }

    override fun getTotalCaloriesBurned(): LiveData<Int> {
        return trackingDao.getTotalCaloriesBurned()
    }

    override fun getTotalDistance(): LiveData<Int> {
        return trackingDao.getTotalDistance()
    }

    override fun getTotalAvgSpeed(): LiveData<Float> {
        return trackingDao.getTotalAvgSpeed()
    }
}