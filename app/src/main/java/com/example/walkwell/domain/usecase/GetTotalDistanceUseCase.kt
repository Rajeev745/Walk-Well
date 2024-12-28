package com.example.walkwell.domain.usecase

import androidx.lifecycle.LiveData
import com.example.walkwell.domain.repository.TrackingRepository

class GetTotalDistanceUseCase(private val trackingRepository: TrackingRepository) {

    operator fun invoke(): LiveData<Int> {
        return trackingRepository.getTotalDistance()
    }
}