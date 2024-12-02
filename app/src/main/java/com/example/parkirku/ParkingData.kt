package com.example.parkirku

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ParkingData(viewModel: FirestoreViewModel) {
    val warnaParkir = remember { mutableStateMapOf<String, Color>() }


    val authViewModel: AuthViewModel = viewModel()
    val ParkingViewModel: FirestoreViewModel = viewModel()

    // Fungsi untuk menyimpan data ke Firestore
    fun updateFirestore(idCCTV: String, idParkir: String, parkirPosition: Boolean, terisi: Boolean) {
        viewModel.updateParkingStatus(idCCTV, idParkir, parkirPosition, terisi) { success ->
            if (success) {
                println("Inserted successfully: $idParkir")
            } else {
                println("Insertion failed: $idParkir")
            }
        }
    }
    fun updateAllParkir() {
        val idCCTV = "F1"
        val status1 = true
        val status2 = true

        var block = 'A'
        while (block <= 'G') {
            for (number in 1..5) {
                val idParkir = block.toString() + number.toString()
                updateFirestore(idCCTV, idParkir, status1, status2)
            }
            block++
        }
    }
    updateAllParkir()

}

