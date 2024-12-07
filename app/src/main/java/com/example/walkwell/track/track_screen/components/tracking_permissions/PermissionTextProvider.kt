package com.example.walkwell.track.track_screen.components.tracking_permissions

interface PermissionTextProvider {

    fun getDescription(isPermanentlyDeclined: Boolean): String
}