package com.profinales.mapasentregable

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.profinales.mapasentregable.screens.Mapa

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar permisos antes de mostrar la UI
        if (checkLocationPermissions()) {
            launchApp()
        } else {
            requestLocationPermissions()
        }
    }

    /**
     * Verifica si los permisos de ubicación están concedidos.
     */
    private fun checkLocationPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Solicita permisos de ubicación al usuario si no están concedidos.
     */
    private fun requestLocationPermissions() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val allPermissionsGranted = permissions.values.all { it }
                if (allPermissionsGranted) {
                    Log.d("MainActivity", "Permisos de ubicación concedidos")
                    launchApp()
                } else {
                    Log.e("MainActivity", "Permisos de ubicación denegados")
                    finish() // Cierra la app si el usuario no concede permisos
                }
            }

        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    /**
     * Lanza la UI de la aplicación después de verificar los permisos.
     */
    private fun launchApp() {
        setContent {
            Mapa()
        }
    }
}
