package com.example.parkirku

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.parkirku.ParkingData

import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource

//ganti versi material 3
@Composable
fun MainScreen() {
    val currentPage = remember { mutableStateOf("Beranda") }
    val secondaryColor = colorResource(id = R.color.secondary_color)
    val quaternaryColor = colorResource(id = R.color.quaternary_color)

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column(modifier = Modifier.height(120.dp)) {
                Box( modifier = Modifier .fillMaxWidth() .height(1.dp).background(Color.Gray))
                NavigationBar(modifier = Modifier
                    .background(Color.White),
                Color.White) {
                NavigationBarItem(
                    selected = currentPage.value == "Panduan",
                    onClick = { currentPage.value = "Panduan" },
                    label = { Text("Panduan") },
                    icon = {
                        Icon(Icons.Filled.Book,
                            contentDescription = "Panduan",
                            tint = if (currentPage.value == "Panduan") secondaryColor else quaternaryColor,
                            modifier = Modifier.size(35.dp))
                    },colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = secondaryColor,
                        selectedTextColor = secondaryColor,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = quaternaryColor,
                        unselectedTextColor = quaternaryColor)
                )
                NavigationBarItem(
                    selected = currentPage.value == "Beranda",
                    onClick = { currentPage.value = "Beranda" },
                    label = { Text("Beranda") },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Beranda",
                        tint = if (currentPage.value == "Beranda") secondaryColor else quaternaryColor,
                        modifier = Modifier.size(35.dp) )
                    },colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = secondaryColor,
                        selectedTextColor = secondaryColor,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = quaternaryColor,
                        unselectedTextColor = quaternaryColor)

                )
                NavigationBarItem(
                    selected = currentPage.value == "Profile",
                    onClick = { currentPage.value = "Profile" },
                    label = { Text("Profile") },
                    icon = { Icon(Icons.Filled.Person,
                        contentDescription = "Profile",
                        tint = if (currentPage.value == "Profile") secondaryColor else quaternaryColor,
                        modifier = Modifier.size(35.dp))
                    },colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = secondaryColor,
                        selectedTextColor = secondaryColor,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = quaternaryColor,
                        unselectedTextColor = quaternaryColor)
                )
            }
            }

        }
    ) { innerPadding ->
        // Gunakan innerPadding untuk konten
        val authViewModel: AuthViewModel = viewModel()
        val ParkingViewModel: FirestoreViewModel = viewModel()
        when (currentPage.value) {
            "Panduan" -> PanduanPage(Modifier.padding(innerPadding))
            "Beranda" -> BerandaPage(Modifier.padding(innerPadding),documentId = "F1", viewModel = ParkingViewModel,  authViewModel = authViewModel, context = LocalContext.current)
            "Profile" -> ProfilePage(Modifier.padding(innerPadding), viewModel = authViewModel, context = LocalContext.current)
        }
    }
}



