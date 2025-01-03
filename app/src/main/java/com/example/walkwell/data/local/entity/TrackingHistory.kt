package com.example.walkwell.data.local.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Running_table")
class TrackingHistory(
    val image: Bitmap? = null,
    val timeStamp: Long? = 0L,
    var avgSpeedInKMH: Float? = 0f,
    var distanceInMeters: Int? = 0,
    var timeInMillis: Long? = 0L,
    var caloriesBurned: Int? = 0,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}