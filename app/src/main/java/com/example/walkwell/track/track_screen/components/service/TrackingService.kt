package com.example.walkwell.track.track_screen.components.service

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.walkwell.R
import com.example.walkwell.utilities.ComponentTags.Companion.LOCATION_INTERVAL_REQUEST
import com.example.walkwell.utilities.ComponentTags.Companion.PAUSE_SERVICE
import com.example.walkwell.utilities.ComponentTags.Companion.START_SERVICE
import com.example.walkwell.utilities.ComponentTags.Companion.STOP_SERVICE
import com.example.walkwell.utilities.ComponentTags.Companion.TRACKING_NOTIFICATION_CHANNEL_ID
import com.example.walkwell.utilities.UtilitiesFunctions.formatElapsedTime
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * TrackingService is a foreground service responsible for tracking the user's location during a workout.
 * It leverages the FusedLocationProviderClient to receive location updates and manages the lifecycle of
 * these updates. The service can be started, resumed, paused, or stopped based on user actions, and it
 * handles the creation of a persistent notification to ensure the service isn't terminated by the system.
 *
 * The service observes a MutableLiveData object, `isTracking`, to update location tracking status.
 */
@AndroidEntryPoint
class TrackingService : LifecycleService() {

    // Coroutine scope for service to perform background work
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    // Job reference for coroutine tasks, can be null initially
    private var job: Job? = null
    
    // Manager for handling notifications pushed by the service
    private lateinit var notificationManager: NotificationManager
    
    // Builder object to configure and build notifications
    private lateinit var notification: NotificationCompat.Builder

    // LiveData to track time elapsed in seconds during tracking
    private val timeRunInSeconds = MutableLiveData<Long>()

    // Starting timestamp in milliseconds when the tracking begins
    private var startTime = 0L
    
    // Time already elapsed before pausing, used for resume functionality
    private var elapsedTime = 0L
    
    // Scope specifically for managing timer functionalities on the Main thread
    private val timerScope = CoroutineScope(Dispatchers.Main + Job())
    
    // Flag to check if the tracking service is run for the first time
    private var isFirstRun = true

    // Client to interact with location services for getting updates
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    // Configuration for location requests with high accuracy and specific interval
    private val locationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setIntervalMillis(LOCATION_INTERVAL_REQUEST)
            .build()
    }

    // Callback to handle location results and availability updates
    private val locationCallback by lazy {
        object : LocationCallback() {
            // Called when the availability of location data changes
            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
            }

            // Called when a new location result is available
            override fun onLocationResult(location: LocationResult) {
                super.onLocationResult(location)
                // Update path points if tracking is active
                if (isTracking.value == true) {
                    location.locations.forEach { loc ->
                        val newPoint = listOf(loc.latitude, loc.longitude)
                        val updatedPoints = pathPoints.value.toMutableList()
                        updatedPoints.add(newPoint)
                        _pathPoints.tryEmit(updatedPoints) // Emit the new path points
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            START_SERVICE -> startTrackingService()
            STOP_SERVICE -> stopTrackingService()
            PAUSE_SERVICE -> pauseTrackingService()
            else -> Unit
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        stopSelf()
        timerScope.cancel()
    }

    /**
     * Initializes tracking-related values.
     * Sets the tracking status to false and clears any existing path points.
     */
    private fun postInitialValues() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        isTracking.postValue(false)
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * Starts the tracking service in the foreground with a notification.
     * This method checks for POST_NOTIFICATIONS permission if the device is running on
     * Android Oreo (API level 26) or higher. If the permission is granted, it starts
     * the service in the foreground with a persistent notification displaying a tracking status.
     * The notification uses a small icon and shows default text with a time placeholder.
     *
     * It builds a notification using NotificationCompat.Builder and associates it with
     * the TRACKING_NOTIFICATION_CHANNEL_ID. The notification is marked as ongoing to
     * prevent it from being dismissed by the user, ensuring that the service persists.
     */
    private fun startTrackingService() {

        isTracking.postValue(true)

        getLocationUpdates()
        notification = NotificationCompat.Builder(this, TRACKING_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Tracking Service")
            .setContentText("00:00:00")
            .setOngoing(true)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_notification_icon)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startForeground(1, notification.build())
            }
        } else {
            startForeground(1, notification.build())
        }
        startTimer()
    }

    /**
     * Stops the tracking service.
     * This method stops the service that is running in the foreground
     * and updates the tracking status to false. It also stops the service itself.
     */
    private fun stopTrackingService() {
        resetStopwatch()
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        _pathPoints.tryEmit(mutableListOf())
        stopForeground(true)
        stopSelf()
        isTracking.postValue(false)
    }

    @SuppressLint("MissingPermission")
    /**
     * Requests location updates from the FusedLocationProviderClient.
     * This method sets up the locationRequest with a high accuracy priority
     * and binds it to the locationCallback. It utilizes the mainLooper
     * to handle the location updates in the main thread.
     *
     * The location updates are only requested if the fusedLocationProviderClient is not null.
     */
    private fun getLocationUpdates() {
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            mainLooper
        )
    }

    /**
     * Pauses the stopwatch.
     * Cancels all the child coroutines of the timerScope, effectively pausing
     * the ongoing stopwatch operation, stopping the countdown/timer without resetting it.
     */
    private fun pauseStopwatch() {
        timerScope.coroutineContext.cancelChildren()
    }

    /**
     * Resets the stopwatch.
     * Calls pauseStopwatch to halt any ongoing timer operation, resets the elapsed time
     * to zero, and updates the notification to reflect the reset time.
     */
    private fun resetStopwatch() {
        pauseStopwatch()
        elapsedTime = 0L
        timeRunInMillis.postValue(0L)
        updateNotification("00:00:00")
    }

    /**
     * Pauses the tracking service.
     * This method temporarily pauses the tracking by setting the tracking
     * status to false without stopping the service itself.
     */
    private fun pauseTrackingService() {
        pauseStopwatch()
        isTracking.postValue(false)
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }

    /**
     * Starts the timer for tracking.
     * This method initializes the startTime with the current system time
     * added to any previously elapsed time and begins a coroutine to
     * update the notification periodically with the elapsed time.
     */
    private fun startTimer() {
        if (isFirstRun) {
            startTime = System.currentTimeMillis() + elapsedTime
            isFirstRun = false
        } else {
            startTime = System.currentTimeMillis() - elapsedTime
        }
        timerScope.launch {
            while (isActive) {
                elapsedTime = System.currentTimeMillis() - startTime
                timeRunInMillis.postValue(elapsedTime)
                updateNotification(formatElapsedTime(elapsedTime))
                delay(1000) // Update every second
            }
        }
    }

    /**
     * Updates the notification with the provided string.
     * This method changes the content text of the notification and publishes
     * it to the NotificationManager to reflect the updated tracking status.
     *
     * @param string The string to update the notification with.
     */
    private fun updateNotification(string: String) {
        notification.setContentText(string)
        notificationManager.notify(1, notification.build())
    }

    companion object {
        val isTracking = MutableLiveData<Boolean>(false)
        val timeRunInMillis = MutableLiveData<Long>()

        // MutableStateFlow for path points
        private val _pathPoints = MutableStateFlow<List<List<Double>>>(emptyList())
        val pathPoints: StateFlow<List<List<Double>>> get() = _pathPoints
    }
}