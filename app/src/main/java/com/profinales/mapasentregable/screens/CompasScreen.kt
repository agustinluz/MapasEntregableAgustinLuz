package com.profinales.mapasentregable.screens


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.profinales.mapasentregable.MVVM.CompassViewModel

@Composable
fun CompassScreen(navigationController: NavHostController) {
    val viewModel: CompassViewModel = viewModel()
    val norteAngulo = viewModel.norte.observeAsState(0f)

    val paint = remember {
        android.graphics.Paint().apply {
            textSize = 60f
            color = android.graphics.Color.BLACK
            textAlign = android.graphics.Paint.Align.CENTER
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2

            drawContext.canvas.nativeCanvas.apply {
                drawText("N", centerX, 100f, paint)
                drawText("S", centerX, size.height - 50f, paint)
                drawText("E", size.width - 50f, centerY, paint)
                drawText("O", 50f, centerY, paint)
            }

            rotate(degrees = -norteAngulo.value, pivot = center) {
                drawLine(
                    color = Color.Red,
                    start = center,
                    end = center.copy(y = center.y - 200f),
                    strokeWidth = 8f
                )
            }
        }

        Text(
            text = "NORTE: ${norteAngulo.value}°",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}