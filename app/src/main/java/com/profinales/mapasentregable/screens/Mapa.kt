package com.profinales.mapasentregable.screens

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.profinales.mapasentregable.MVVM.UbicacionViewModel

/**
 * Pantalla del mapa donde se muestra la ubicación en tiempo real del usuario.
 */
@Composable
fun Mapa(viewModel: UbicacionViewModel = viewModel()) {

    // Obtenemos la ubicación actual del usuario desde el ViewModel
    val ubicacionAct by viewModel.ubicacionAct.observeAsState()

    // Estado de la cámara del mapa
    val camaraPositionState = rememberCameraPositionState {
        // Si hay ubicación disponible, centramos la cámara en ella
        ubicacionAct?.let {
            position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude), 15f
            )
        }
    }

    // Iniciamos la escucha de la ubicación cuando la pantalla se abre
    LaunchedEffect(Unit) {
        viewModel.iniciarActualizacionUbicacion()
    }

    // Detenemos la escucha de la ubicación cuando la pantalla se cierra
    DisposableEffect(Unit) {
        onDispose { viewModel.detenerActualizacionUbicacion() }
    }

    // UI del mapa
    Box(modifier = Modifier.fillMaxSize()) {

        // Mapa de Google
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = camaraPositionState
        ) {
            // Si tenemos una ubicación, mostramos un marcador en ella
            ubicacionAct?.let { location ->
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = "TU"
                )
            }
        }
    }
}
