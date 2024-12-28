package com.example.walkwell.domain.usecase

data class TrackingUseCase(
    val insertTrackingUseCase: InsertTrackingUseCase,
    val deleteTrackingUseCase: DeleteTrackingUseCase,
    val getAllTrackingSortedByDateUseCase: GetAllTrackingSortedByDateUseCase,
    val getAllTrackingSortedByAvgSpeedUseCase: GetAllTrackingSortedByAvgSpeedUseCase,
    val getAllTrackingSortedByCalorieRunUseCase: GetAllTrackingSortedByCalorieRunUseCase,
    val getAllTrackingSortedByDistanceUseCase: GetAllTrackingSortedByDistanceUseCase,
    val getTotalDistanceUseCase: GetTotalDistanceUseCase,
    val getTotalAvgSpeedUseCase: GetTotalAvgSpeedUseCase,
    val getTotalCaloriesBurnedUseCase: GetTotalCaloriesBurnedUseCase,
    val getTotalTimeInMillisUseCase: GetTotalTimeInMillisUseCase
)