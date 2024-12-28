package com.example.walkwell.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.walkwell.data.local.dao.TrackingDao
import com.example.walkwell.data.local.entity.TrackingHistory

@Database(
    entities = [TrackingHistory::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TrackingDatabase: RoomDatabase() {

    abstract fun getTrackingDao(): TrackingDao
}