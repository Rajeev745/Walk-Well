package com.example.walkwell.domain.usecase

import androidx.lifecycle.LiveData
import com.example.walkwell.data.local.entity.TrackingHistory
import com.example.walkwell.domain.repository.TrackingRepository

class GetAllTrackingSortedByAvgSpeedUseCase(private val trackingRepository: TrackingRepository) {

    operator fun invoke(): LiveData<List<TrackingHistory>> {
        return trackingRepository.getAllRunsSortedByAvgSpeed()
    }

}