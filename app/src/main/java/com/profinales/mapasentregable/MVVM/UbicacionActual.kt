package com.profinales.mapasentregable.MVVM
import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*

/**
 * ViewModel para manejar la ubicación en tiempo real del usuario.
 */
class UbicacionViewModel(application: Application) : AndroidViewModel(application) {

    // Cliente para obtener la ubicación del usuario
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    // Variable para almacenar la ubicación en vivo
    private val _ubicacionAct = MutableLiveData<Location?>()
    val ubicacionAct: LiveData<Location?> = _ubicacionAct

    // Configuración de la solicitud de ubicación
    private val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    // Callback que se ejecuta cuando la ubicación cambia
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // Guardamos la última ubicación obtenida
            locationResult.lastLocation?.let { location ->
                _ubicacionAct.value = location
            }
        }
    }

    /**
     * Inicia la actualización de la ubicación.
     */
    @SuppressLint("MissingPermission")
    fun iniciarActualizacionUbicacion() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    /**
     * Detiene la actualización de la ubicación.
     */
    fun detenerActualizacionUbicacion() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
