package com.profinales.mapasentregable.screens

import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.profinales.mapasentregable.MVVM.UbicacionViewModel

/**
 * Pantalla del mapa donde se muestra la ubicación en tiempo real del usuario
 * y permite guardar ubicaciones en Firebase Firestore.
 */
@Composable
fun Mapa(viewModel: UbicacionViewModel = viewModel()) {

    val ubicacionActual by viewModel.ubicacionAct.observeAsState()

    val camaraPositionState = rememberCameraPositionState {
        ubicacionActual?.let {
            position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude), 15f
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.iniciarActualizacionUbicacion()
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.detenerActualizacionUbicacion() }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = camaraPositionState
        ) {
            ubicacionActual?.let { location ->
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = "Tu Ubicación"
                )
            }
        }

        // Botón para guardar ubicación
        Button(
            onClick = { viewModel.guardarUbicacionEnFirebase() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Guardar Ubicación")
        }
    }
}
