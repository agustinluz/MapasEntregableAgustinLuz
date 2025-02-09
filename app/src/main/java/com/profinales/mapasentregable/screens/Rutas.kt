package com.profinales.mapasentregable.screens

sealed class Ruta(val ruta:String) {

    object Mapa:Ruta("Mapa")
    object CompassScreen:Ruta("CompassScreen")

}