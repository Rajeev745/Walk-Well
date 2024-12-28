package com.example.walkwell.domain.repository

import androidx.lifecycle.LiveData
import com.example.walkwell.data.local.entity.TrackingHistory

interface TrackingRepository {

    suspend fun insertTracking(trackingHistory: TrackingHistory)

    suspend fun deleteTracking(trackingHistory: TrackingHistory)

    fun getAllRunsSortedByDate(): LiveData<List<TrackingHistory>>

    fun getAllRunsSortedByTimeInMillis(): LiveData<List<TrackingHistory>>

    fun getAllRunsSortedByCaloriesBurned(): LiveData<List<TrackingHistory>>

    fun getAllRunsSortedByAvgSpeed(): LiveData<List<TrackingHistory>>

    fun getAllRunsSortedByDistance(): LiveData<List<TrackingHistory>>

    fun getTotalTimeInMillis(): LiveData<Long>

    fun getTotalCaloriesBurned(): LiveData<Int>

    fun getTotalDistance(): LiveData<Int>

    fun getTotalAvgSpeed(): LiveData<Float>
}