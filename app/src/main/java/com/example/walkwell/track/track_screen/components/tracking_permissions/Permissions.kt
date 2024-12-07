package com.example.walkwell.track.track_screen.components.tracking_permissions

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.example.walkwell.track.track_screen.components.TrackingViewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun Permissions(trackingViewmodel: TrackingViewModel) {

    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    val dialogQueue = trackingViewmodel.visiblePermissionDialogQueue

    var shouldRequestPermission by rememberSaveable { mutableStateOf(true) }

    val permissionToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val multiplePermissionResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { params ->
                permissionToRequest.forEach { permission ->
                    trackingViewmodel.onPermissionResult(
                        permission, params[permission] == true
                    )
                }
            })

    // Check permissions and request if necessary
    LaunchedEffect(Unit) {
        if (shouldRequestPermission) {
            val allPermissionsGranted = permissionToRequest.all { permission ->
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }
            trackingViewmodel.updatePermissionsGrantedState(allPermissionsGranted)
            if (!allPermissionsGranted) {
                multiplePermissionResultLauncher.launch(permissionToRequest)
            }
            shouldRequestPermission = false
        }
    }

    dialogQueue.reversed().forEach { permission ->

        PermissionDialog(permissionTextProvider = when (permission) {
            Manifest.permission.ACCESS_COARSE_LOCATION -> {
                TrackLocationPermissionProvider()
            }

            Manifest.permission.ACCESS_FINE_LOCATION -> {
                TrackLocationPermissionProvider()
            }

            Manifest.permission.ACCESS_BACKGROUND_LOCATION -> {
                BackgroundLocationPermissionProvider()
            }

            else -> return@forEach
        },
            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(activity, permission),
            onDismiss = trackingViewmodel::dismissDialog,
            onOkClick = {
                trackingViewmodel.dismissDialog()
                multiplePermissionResultLauncher.launch(arrayOf(permission))
            },
            onGoToAppSettingsClick = { activity.openAppSettings() })
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}