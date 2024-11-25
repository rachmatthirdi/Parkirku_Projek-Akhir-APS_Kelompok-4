
package com.example.parkirku

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirestoreViewModel : ViewModel() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun updateParkingStatus(
        idCCTV: String,
        idParkir: String,
        parkirPosition: Boolean,
        terisi: Boolean,
        onResult: (Boolean) -> Unit
    ) {
        val data = hashMapOf(
            "idParkir" to idParkir,
            "parkirPosition" to parkirPosition,
            "terisi" to terisi
        )

        firestore.collection("parkingStatus").document(idCCTV)
            .set(mapOf(idParkir to data), SetOptions.merge())
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun getAllParkingStatus(
        idCCTV: String,
        onResult:(Map<String, Map<String, Any>>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val documentSnapshot = firestore.collection("parkingStatus").document(idCCTV).get().await()

                val data = documentSnapshot.data ?: emptyMap()
                val statusParkir = data.mapValues { (_, value) ->
                    value as Map<String, Any>
                }
                onResult(statusParkir)
            } catch (e: Exception) {
                Log.e("Firestore", "Error fetching data", e)
                onResult(emptyMap())
            }
        }
    }
}

//class FirestoreViewModel : ViewModel() {
//    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//
//    fun updateParkingStatus(
//        idCCTV: String,
//        idParkir: String,
//        parkirPosition: Boolean,
//        terisi: Boolean,
//        onResult: (Boolean) -> Unit
//    ) {
//        // Data yang akan disimpan di Firestore
//        val data = hashMapOf(
//            "idParkir" to idParkir,
//            "parkirPosition" to parkirPosition,
//            "terisi" to terisi
//        )
//
//        firestore.collection("parkingStatus").document(idCCTV)
//            .set(mapOf(idParkir to data), SetOptions.merge())
//            .addOnSuccessListener {
//                onResult(true)
//            }
//            .addOnFailureListener {
//                onResult(false)
//            }
//    }
//
//    fun getAllParkingStatus(
//        idCCTV: String,
//        onResult: (Map<String, Map<String, Any>>) -> Unit
//    ) {
//        viewModelScope.launch {
//            try {
//                val documentSnapshot = firestore.collection("parkingStatus").document(idCCTV).get().await()
//
//                val data = documentSnapshot.data ?: emptyMap()
//                val statusParkir = data.mapValues { (_, value) ->
//                    value as Map<String, Any>
//                }
//                onResult(statusParkir)
//            } catch (e: Exception) {
//                Log.e("Firestore", "Error fetching data", e)
//                onResult(emptyMap())
//            }
//        }
//    }
//}

