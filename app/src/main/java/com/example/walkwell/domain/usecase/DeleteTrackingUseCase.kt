package com.example.walkwell.domain.usecase

import com.example.walkwell.data.local.entity.TrackingHistory
import com.example.walkwell.domain.repository.TrackingRepository

class DeleteTrackingUseCase(private val trackingRepository: TrackingRepository) {

    suspend operator fun invoke(trackingHistory: TrackingHistory) {
        trackingRepository.deleteTracking(trackingHistory)
    }
}