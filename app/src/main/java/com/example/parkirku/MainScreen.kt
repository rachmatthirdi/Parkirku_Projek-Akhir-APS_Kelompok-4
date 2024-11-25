package com.example.parkirku

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.parkirku.ParkingData

import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.platform.LocalContext

//ganti versi material 3
@Composable
fun MainScreen() {
    val currentPage = remember { mutableStateOf("Beranda") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentPage.value == "Panduan",
                    onClick = { currentPage.value = "Panduan" },
                    label = { Text("Panduan") },
                    icon = { Icon(Icons.Filled.Info, contentDescription = "Panduan") }
                )
                NavigationBarItem(
                    selected = currentPage.value == "Beranda",
                    onClick = { currentPage.value = "Beranda" },
                    label = { Text("Beranda") },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Beranda") }
                )
                NavigationBarItem(
                    selected = currentPage.value == "Profile",
                    onClick = { currentPage.value = "Profile" },
                    label = { Text("Profile") },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") }
                )
            }
        }
    ) { innerPadding ->
        // Gunakan innerPadding untuk konten
        val authViewModel: AuthViewModel = viewModel()
        val ParkingViewModel: FirestoreViewModel = viewModel()
        when (currentPage.value) {
            "Panduan" -> PanduanPage(Modifier.padding(innerPadding))
            "Beranda" -> BerandaPage(documentId = "F1", viewModel = ParkingViewModel,  authViewModel = authViewModel, context = LocalContext.current)
            "Profile" -> ProfilePage(Modifier.padding(innerPadding), viewModel = authViewModel, context = LocalContext.current)
        }
    }
}

//@Composable
//fun MainScreen() {
//    val currentPage = remember { mutableStateOf("Beranda") }
//
//    Scaffold(
//        bottomBar = {
//            NavigationBar {
//                NavigationBarItem(
//                    selected = currentPage.value == "Panduan",
//                    onClick = { currentPage.value = "Panduan" },
//                    label = { Text("Panduan") },
//                    icon = { Icon(Icons.Filled.Info, contentDescription = "Panduan") }
//                )
//                NavigationBarItem(
//                    selected = currentPage.value == "Beranda",
//                    onClick = { currentPage.value = "Beranda" },
//                    label = { Text("Beranda") },
//                    icon = { Icon(Icons.Filled.Home, contentDescription = "Beranda") }
//                )
//                NavigationBarItem(
//                    selected = currentPage.value == "Profile",
//                    onClick = { currentPage.value = "Profile" },
//                    label = { Text("Profile") },
//                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") }
//                )
//            }
//        }
//    ) { innerPadding ->
//        // Gunakan innerPadding untuk konten
//        val ParkingViewModel: FirestoreViewModel =viewModel()
//        when (currentPage.value) {
//            "Panduan" -> PanduanPage(Modifier.padding(innerPadding))
//            "Beranda" -> BerandaPage(documentId = "F1", viewModel = ParkingViewModel)
//            "Profile" -> ProfilePage(Modifier.padding(innerPadding))
//        }
//    }
//}



