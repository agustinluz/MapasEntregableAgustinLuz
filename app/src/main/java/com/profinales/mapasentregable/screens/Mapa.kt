package com.profinales.mapasentregable.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.*
import com.profinales.mapasentregable.MVVM.LocationViewModel

/**
 * Pantalla del mapa donde se muestra la ubicación en tiempo real del usuario
 * y las ubicaciones guardadas en Firebase Firestore.
 */
@Composable
fun Mapa(viewModel: LocationViewModel = viewModel()) {

    val ubicacionActual by viewModel.ubicacionAct.observeAsState()
    val ubicacionesGuardadas by viewModel.ubicacionesGuardadas.observeAsState(emptyList())

    val camaraPositionState = rememberCameraPositionState {
        ubicacionActual?.let {
            position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude), 15f
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.iniciarActualizacionUbicacion()
        viewModel.escucharUbicaciones()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.detenerActualizacionUbicacion()
            viewModel.detenerEscuchaUbicaciones()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = camaraPositionState
        ) {

            // Mostrar la ubicación actual con un marcador azul
            ubicacionActual?.let { location ->
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = "Tu Ubicación",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                )
            }

            // Mostrar todas las ubicaciones guardadas en Firebase con marcadores
            ubicacionesGuardadas.forEach { ubicacion ->
                Marker(
                    state = MarkerState(position = LatLng(ubicacion.latitud, ubicacion.longitud)),
                    title = "Ubicación Guardada"
                )
            }

            // Dibujar una línea entre los puntos para representar la ruta
            if (ubicacionesGuardadas.size > 1) {
                Polyline(
                    points = ubicacionesGuardadas.map { LatLng(it.latitud, it.longitud) },
                    color = Color.Blue,
                    width = 5f
                )
            }
        }

        // Botón para guardar la ubicación actual
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
