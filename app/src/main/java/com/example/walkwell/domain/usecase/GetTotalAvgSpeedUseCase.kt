package com.example.walkwell.domain.usecase

import androidx.lifecycle.LiveData
import com.example.walkwell.domain.repository.TrackingRepository

class GetTotalAvgSpeedUseCase(private val trackingRepository: TrackingRepository) {

    operator fun invoke(): LiveData<Float> {
        return trackingRepository.getTotalAvgSpeed()
    }

}