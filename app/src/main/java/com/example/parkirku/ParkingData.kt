package com.example.parkirku

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import java.sql.Array
import java.util.LinkedList
import java.util.Queue

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


    // Menyimpan beberapa data
    updateFirestore("F1", "A5", true, true)
    updateFirestore("F1", "A3", true, true)
    updateFirestore("F1", "A4", true, false)
    updateFirestore("F1", "A2", true, true)
    // Menampilkan halaman utama
    BerandaPage(documentId = "F1", viewModel = ParkingViewModel,  authViewModel = authViewModel, context = LocalContext.current)
}

// Create an array of ParkingSpot



//@Composable
//fun ParkingData(viewModel: FirestoreViewModel) {
//
//    val warnaParkir = remember { mutableStateMapOf<String, Color>() }
//
//    // Fungsi untuk memperbarui status parkir di Firestore
//    fun updateFirestore(idCCTV: String, idParkir: String, parkirPosition: Boolean, terisi: Boolean) {
//
//        viewModel.updateParkingStatus(idCCTV, idParkir,parkirPosition, terisi, warnaParkir ) { success ->
//            if (success) {
//                println("inserted successfully")
//            } else {
//                println("inserted failed")
//            }
//        }
//    }
//
//    updateFirestore("F1","A5",true,true)
//    updateFirestore("F1","A3",true,true)
//    updateFirestore("F1","A4",true,true)
//
//    BerandaPage(documentId = "F1")
//
//}

//    BerandaPage(
//        modifier = Modifier,
//        parkirPath = dummyArray,
//        idParkir = "A5",
//        parkirPosition = true,
//        terisi = false
//    )
//    updateFirestore("A5")


