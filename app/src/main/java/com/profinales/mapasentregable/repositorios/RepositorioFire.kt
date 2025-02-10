package com.profinales.mapasentregable.repositorios

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject

/**
 * Modelo de datos para una ubicación.
 */
data class Ubicacion(
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)

/**
 * Repositorio para manejar Firebase Firestore.
 */
class RepositorioFire {

    private val db = FirebaseFirestore.getInstance()
    private val ubicacionesRef = db.collection("ubicaciones")

    private var listener: ListenerRegistration? = null

    /**
     * Guarda una ubicación en Firebase Firestore.
     */
    fun guardarUbicacion(ubicacion: Ubicacion) {
        ubicacionesRef.add(ubicacion)
            .addOnSuccessListener { Log.d("RepositorioFire", "Ubicación guardada correctamente") }
            .addOnFailureListener { e -> Log.e("RepositorioFire", "Error al guardar ubicación", e) }
    }

    /**
     * Obtiene las ubicaciones en tiempo real y ejecuta el callback cuando hay cambios.
     */
    fun escucharUbicacionesEnTiempoReal(onUbicacionesActualizadas: (List<Ubicacion>) -> Unit) {
        listener = ubicacionesRef.addSnapshotListener { snapshot, _ ->
            val ubicaciones = snapshot?.documents?.mapNotNull { it.toObject<Ubicacion>() } ?: emptyList()
            onUbicacionesActualizadas(ubicaciones)
        }
    }

    /**
     * Detiene la escucha de cambios en Firestore.
     */
    fun detenerEscucha() {
        listener?.remove()
    }
}
