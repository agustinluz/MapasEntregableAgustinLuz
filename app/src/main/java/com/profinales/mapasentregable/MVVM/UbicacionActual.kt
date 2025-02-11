package com.profinales.mapasentregable.MVVM

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.profinales.mapasentregable.repositorios.RepositorioFire
import com.profinales.mapasentregable.repositorios.Ubicacion

/**
 * ViewModel para manejar la ubicación en tiempo real y mostrar rutas en el mapa.
 */
class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _ubicacionActual = MutableLiveData<Location?>()
    val ubicacionAct: LiveData<Location?> = _ubicacionActual

    private val _ubicacionesGuardadas = MutableLiveData<List<Ubicacion>>()
    val ubicacionesGuardadas: LiveData<List<Ubicacion>> = _ubicacionesGuardadas

    private val repository = RepositorioFire()

    private val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 5000
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                _ubicacionActual.value = location
            }
        }
    }

    fun iniciarActualizacionUbicacion() {
        val context = getApplication<Application>().applicationContext
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("LocationViewModel", "Permisos de ubicación no concedidos")
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun detenerActualizacionUbicacion() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * Guarda la ubicación actual en Firebase Firestore.
     */
    fun guardarUbicacionEnFirebase() {
        _ubicacionActual.value?.let { location ->
            val ubicacion = Ubicacion(location.latitude.toString(), location.longitude)
            repository.guardarUbicacion(ubicacion)
        }
    }
    fun eliminarUbicacion(id: String) {
        repository.eliminarUbicacion(id)
    }
    fun actualizarUbicacion(id: String, nuevaUbicacion: Ubicacion) {
        repository.actualizarUbicacion(id, nuevaUbicacion)
    }
    /**
     * Escucha ubicaciones en tiempo real desde Firebase.
     */
    fun escucharUbicaciones() {
        repository.escucharUbicacionesEnTiempoReal { ubicaciones ->
            _ubicacionesGuardadas.postValue(ubicaciones)
        }
    }

    /**
     * Detiene la escucha de ubicaciones.
     */
    fun detenerEscuchaUbicaciones() {
        repository.detenerEscucha()
    }

    fun guardarUbicacionClicada(latLng: com.google.android.gms.maps.model.LatLng) {
        val ubicacion = Ubicacion(latLng.latitude.toString(), latLng.longitude)
        repository.guardarUbicacion(ubicacion)
    }
}