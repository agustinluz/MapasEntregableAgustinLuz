package com.profinales.mapasentregable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.profinales.mapasentregable.componentes.DrawerContent
import com.profinales.mapasentregable.componentes.MiTopAppBar
import com.profinales.mapasentregable.screens.CompassScreen
import com.profinales.mapasentregable.screens.Ruta

import com.profinales.mapasentregable.screens.Mapa
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigationController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    DrawerContent {
                            route -> scope.launch { drawerState.close() }
                        navigationController.navigate(route)
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        MiTopAppBar(onMenuClick = { scope.launch { drawerState.open() } })
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navigationController,
                        startDestination = Ruta.Mapa.ruta,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(Ruta.Mapa.ruta) { Mapa() }
                        composable(Ruta.CompassScreen.ruta) { CompassScreen(navigationController) }

                    }
                }
            }
        }
    }
}