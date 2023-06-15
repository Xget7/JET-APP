package xget.dev.jet.data.util.location

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import xget.dev.jet.JetApp
import xget.dev.jet.MainActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationHelper(val context: Context) {


    val REQUEST_LOCATION = 199




    suspend fun checkLocationSettings(): CheckLocationSettingsResponse = suspendCancellableCoroutine { continuation ->
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            fastestInterval = 2000
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(context)
            .checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                val response: LocationSettingsResponse = task.getResult(ApiException::class.java)
                continuation.resume(CheckLocationSettingsResponse(true, null))
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val resolvableApiException = e as ResolvableApiException

                            continuation.resume(CheckLocationSettingsResponse(true, IntentSenderRequest.Builder(resolvableApiException.resolution).build()))




                        } catch (ex: IntentSender.SendIntentException) {
                            ex.printStackTrace()
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // Device does not have location
                        continuation.resume(CheckLocationSettingsResponse(true, null))
                    }
                }
            }
        }

        continuation.invokeOnCancellation {

        }
    }



}

data class CheckLocationSettingsResponse(
    val enabled : Boolean,
    val intentSenderForResult:  IntentSenderRequest?
)