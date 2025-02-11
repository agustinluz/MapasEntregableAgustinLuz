package com.profinales.mapasentregable.componentes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.profinales.mapasentregable.MVVM.LocationViewModel

@Composable
fun BotonFlotante(viewModel: LocationViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = { viewModel.guardarUbicacionEnFirebase() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Guardar Ubicación")
        }
    }
}