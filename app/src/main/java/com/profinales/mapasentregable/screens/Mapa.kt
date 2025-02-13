package com.profinales.mapasentregable.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
//CHEETO
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.profinales.mapasentregable.componentes.BotonFlotante
import com.profinales.mapasentregable.repositorios.Ubicacion
import androidx.compose.foundation.lazy.items



/**
 * Pantalla del mapa donde se muestra la ubicación en tiempo real del usuario
 * y las ubicaciones guardadas en Firebase Firestore.
 */
@Composable
fun Mapa(viewModel: LocationViewModel = viewModel()) {

    val ubicacionActual by viewModel.ubicacionAct.observeAsState()
    val ubicacionesGuardadas by viewModel.ubicacionesGuardadas.observeAsState(emptyList())
    val coloresRutas = listOf(Color.Blue, Color.Red, Color.Green, Color.Magenta)


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
        // Mapa de Google
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = camaraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true
            ),
            onMapClick = { latLng ->
                viewModel.guardarUbicacionClicada(latLng)
            }
        ) {
            ubicacionActual?.let { location ->
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = "Tu Ubicación",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                )
            }

            ubicacionesGuardadas.forEach { ubicacion ->
                Marker(
                    state = MarkerState(position = LatLng(ubicacion.latitud, ubicacion.longitud)),
                    title = "Ubicación Guardada"
                )
            }
        }

        // Lista de ubicaciones sobre el mapa
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter) // Colocar en la parte superior
        ) {
            items(ubicacionesGuardadas, key = { it.id }) { ubicacion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Lat: ${ubicacion.latitud}, Lng: ${ubicacion.longitud}")
                    IconButton(onClick = { viewModel.eliminarUbicacion(ubicacion.id) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }


        BotonFlotante(viewModel)
    }

}
