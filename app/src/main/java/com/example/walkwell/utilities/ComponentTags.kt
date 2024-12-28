package com.example.walkwell.utilities

class ComponentTags {

    companion object {
        const val HOME_SCREEN_TAG = "home_screen"
        const val HISTORY_SCREEN_TAG = "history_screen"
        const val PROFILE_SCREEN_TAG = "profile_screen"
        const val MAP_TAGS = "map_screen"

        // Notifications
        const val TRACKING_NOTIFICATION_CHANNEL_ID = "tracking_notification_channel_id"
        const val TRACKING_NOTIFICATION_CHANNEL = "tracking_notification_channel"

        // Service
        const val START_SERVICE = "start_tracking_service"
        const val STOP_SERVICE = "stop_tracking_service"
        const val PAUSE_SERVICE = "pause_tracking_service"

        // Timer
        const val TIMER_UPDATE_INTERVAL = 50L

        // Location request interval
        const val LOCATION_INTERVAL_REQUEST = 1000L

        // Database
        const val TRACKING_DATABASE = "tracking_database"
    }
}