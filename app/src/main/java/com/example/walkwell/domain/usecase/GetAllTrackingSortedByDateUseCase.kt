package com.example.walkwell.domain.usecase

import androidx.lifecycle.LiveData
import com.example.walkwell.data.local.entity.TrackingHistory
import com.example.walkwell.domain.repository.TrackingRepository

class GetAllTrackingSortedByDateUseCase(private val trackingRepository: TrackingRepository) {

    suspend operator fun invoke(): LiveData<List<TrackingHistory>> {
        return trackingRepository.getAllRunsSortedByDate()
    }
}