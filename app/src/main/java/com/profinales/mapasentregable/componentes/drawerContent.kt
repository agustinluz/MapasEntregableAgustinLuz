package com.profinales.mapasentregable.componentes


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place

import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place

import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.profinales.mapasentregable.screens.Ruta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(onDestinationClicked: (String) -> Unit) {
    val items = listOf(
        NavigationItem("Pantalla 1", Ruta.Mapa.ruta, Icons.Filled.Place, Icons.Outlined.Place),
        NavigationItem("Brujula", Ruta.CompassScreen.ruta, Icons.Filled.Person, Icons.Outlined.Person)

    )

    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(24.dp))
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(text = item.title) },
                selected = false, // No gestionamos selección aquí
                onClick = { onDestinationClicked(item.route) },
                icon = {
                    Icon(imageVector = item.selectedIcon, contentDescription =item.title)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}


data class NavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)