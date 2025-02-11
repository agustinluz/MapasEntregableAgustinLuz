package com.profinales.mapasentregable.repositorios

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject

/**
 * Modelo de datos para una ubicación.
 */
data class Ubicacion(
    val id: String = " ",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
) {
    override fun toString(): String {
        return "Latitud: $latitud, Longitud: $longitud"
    }
}


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
        val documentRef = ubicacionesRef.document()
        val ubicacionConId = ubicacion.copy(id = documentRef.id)

        documentRef.set(ubicacionConId)
            .addOnSuccessListener { Log.d("RepositorioFire", "Ubicación guardada correctamente") }
            .addOnFailureListener { e -> Log.e("RepositorioFire", "Error al guardar ubicación", e) }
    }
    fun eliminarUbicacion(id: String) {
        ubicacionesRef.document(id).delete()
            .addOnSuccessListener { Log.d("RepositorioFire", "Ubicación eliminada correctamente") }
            .addOnFailureListener { e -> Log.e("RepositorioFire", "Error al eliminar ubicación", e) }
    }
    fun actualizarUbicacion(id: String, nuevaUbicacion: Ubicacion) {
        ubicacionesRef.document(id).set(nuevaUbicacion)
            .addOnSuccessListener { Log.d("RepositorioFire", "Ubicación actualizada correctamente") }
            .addOnFailureListener { e -> Log.e("RepositorioFire", "Error al actualizar ubicación", e) }
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
