package com.profinales.mapasentregable.MVVM


import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CompassViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {

    // SensorManager para acceder a los sensores del dispositivo
    private val sensorManager: SensorManager = application.getSystemService(Application.SENSOR_SERVICE) as SensorManager

    // Sensor de rotación
    private val rotationVectorSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private val _norte = MutableLiveData(0f)
    val norte: LiveData<Float> = _norte

    init {
        rotationVectorSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        try {
            if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
                val rotationMatrix = FloatArray(9)
                val orientation = FloatArray(3)

                // Obtener la matriz de rotación a partir del vector de rotación
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

                // Calcular la orientación del dispositivo
                SensorManager.getOrientation(rotationMatrix, orientation)

                // Convertir el ángulo azimutal de radianes a grados
                _norte.value = Math.toDegrees(orientation[0].toDouble()).toFloat()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No hacemos nada
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }
}
