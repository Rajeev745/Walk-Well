package com.example.walkwell.domain.usecase

import androidx.lifecycle.LiveData
import com.example.walkwell.domain.repository.TrackingRepository

class GetTotalTimeInMillisUseCase(private val trackingRepository: TrackingRepository) {

    operator fun invoke(): LiveData<Long> {
        return trackingRepository.getTotalTimeInMillis()
    }

}