package com.profinales.mapasentregable.repositorios

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

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
class RepositroioFire {

    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()

    // Referencia a la colección "ubicaciones"
    private val ubicacionesRef = db.collection("ubicaciones")

    /**
     * Guarda una ubicación en Firebase Firestore.
     */
    suspend fun guardarUbicacion(ubicacion: Ubicacion) {
        ubicacionesRef.add(ubicacion).await()
    }

    /**
     * Obtiene todas las ubicaciones guardadas en Firebase Firestore.
     */
    suspend fun obtenerUbicaciones(): List<Ubicacion> {
        return try {
            ubicacionesRef.get().await().documents.mapNotNull { it.toObject<Ubicacion>() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
