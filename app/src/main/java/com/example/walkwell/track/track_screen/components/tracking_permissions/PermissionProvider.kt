package com.example.walkwell.track.track_screen.components.tracking_permissions

class TrackLocationPermissionProvider: PermissionTextProvider {

    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined track location permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app need to know your exact location for tracking your walk"
        }
    }
}

class BackgroundLocationPermissionProvider: PermissionTextProvider {

    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined background location permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app need to keep track of your location in background"
        }
    }
}