package com.example.parkirku

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.round
import kotlin.time.Duration.Companion.seconds



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaPage(
    modifier: Modifier = Modifier,
    documentId: String,
    viewModel: FirestoreViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    context: Context
) {
    var idCCTV by remember { mutableStateOf("") }
    var idParkir by remember { mutableStateOf("") }
    var parkirPosition by remember { mutableStateOf(true) }
    var terisi by remember { mutableStateOf(false) }
    val warnaParkir = remember { mutableStateMapOf<String, Color>() }

    //val showNotification by remember { mutableStateOf(false) }



    val parkirPath = pathF1()
    val countPlace = parkirPath.size * parkirPath[0].size
    var emptySlots by remember { mutableStateOf(countPlace) }

    var previousStatus by remember { mutableStateOf(emptySlots > 0) }

    // Check current status
    val currentStatus = emptySlots > 0

    // refresh
    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val numberOfColumns = if (parkirPath.isNotEmpty()) parkirPath[0].size else 0

    val coroutineScope = rememberCoroutineScope()
    var showNotificationDialog by remember { mutableStateOf(false) }

    val user = authViewModel.auth.currentUser
    val userId = user?.uid
    var photoUrl by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    val pesanList = remember { mutableStateMapOf<String, Pair<Boolean, String>>() }
    fun addMessage(isError: Boolean, idParkir: String, newMessage: String) {
        if (pesanList[idParkir]?.second != newMessage) {
            pesanList[idParkir] = Pair(isError, newMessage) }
    }



    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    LaunchedEffect(userId) {
        userId?.let {
            val document = firestore.collection("users").document(it).get().await()
            photoUrl = document.getString("photoUrl") ?: sharedPreferences.getString("photo_url", "") ?: ""
            username = document.getString("name") ?: ""
            email = document.getString("email") ?: ""
            with(sharedPreferences.edit()) {
                putString("photo_url", photoUrl)
                apply()
            }
        }
    }


    LaunchedEffect(documentId) {
        try {
            viewModel.getAllParkingStatus(documentId) { status ->
                var occupiedCount = 0
                status.forEach { (id, data) ->
                    idCCTV = documentId
                    idParkir = id
                    parkirPosition = data["parkirPosition"] as? Boolean ?: false
                    terisi = data["terisi"] as? Boolean ?: false

                    // Log the data for debugging
                    Log.d("FirestoreData", "idCCTV: $idCCTV, idParkir: $idParkir, parkirPosition: $parkirPosition, terisi: $terisi")

                    // Update the parkirPath array based on the status
                    for (row in parkirPath) {
                        for (item in row) {
                            if (item.id.equals(idParkir, ignoreCase = true) && terisi) {
                                item.isOccupied = true

                            } else if (item.id.equals(idParkir, ignoreCase = true) && !terisi) {
                                item.isOccupied = false
                            }

                            if (!parkirPosition) { val newMessage = "Terdapat kesalahan parkir pada $idParkir di wilayah $idCCTV"
                                addMessage(true, idParkir, newMessage)
                            }

                        }
                    }

                    // Update warna parkir berdasarkan kondisi terbaru
                    warnaParkir[idParkir] = if (terisi) Color.Red else Color.Blue

                    // Hitung slot yang terisi
                    if (terisi) {
                        occupiedCount++
                    }
                }
                // Hitung slot yang kosong
                emptySlots = countPlace - occupiedCount
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error fetching data from Firestore", e)
        }
    }


    Scaffold(

        topBar = { // Bagian profil pengguna
            Column {
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(4.dp)
                    ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberImagePainter(photoUrl),
                        contentDescription = "profile",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Black, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(username,modifier = Modifier
                        .padding(2.dp),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image( painter = painterResource(R.drawable.notification_icon),
                        contentDescription = "notif",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                            .clickable {
                                showNotificationDialog = true
                            })
                }

                Box( modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray))

            }


        },content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues). padding(bottom = 120.dp))
            {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "SLOT TERSISA : ${emptySlots}/$countPlace",
                            modifier = Modifier
                                .background(Color.Blue)
                                .padding(10.dp),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        if (currentStatus != previousStatus) {
                            if (currentStatus) {
                                val newMessage = "Parkir kembali kosong"
                                addMessage(false, "status", newMessage) }
                            else { val newMessage = "Parkir penuh"
                                addMessage(false, "status", newMessage) }
                            previousStatus = currentStatus
                        }
                        if (emptySlots == 0) {
                            Text(
                                "Penuh",
                                modifier = Modifier
                                    .background(colorResource(id = R.color.primary_color))
                                    .padding(10.dp),
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    PullToRefreshBox(
                        state = refreshState,
                        modifier = Modifier
                            .background(Color.LightGray, shape = RoundedCornerShape(16.dp)),
                        isRefreshing = isRefreshing,
                        onRefresh = {
                            coroutineScope.launch {
                                isRefreshing = true
                                delay(2000)
                                try {
                                    viewModel.getAllParkingStatus(documentId) { status ->
                                        var occupiedCount = 0
                                        status.forEach { (id, data) ->
                                            idCCTV = documentId
                                            idParkir = id
                                            parkirPosition = data["parkirPosition"] as? Boolean ?: false
                                            terisi = data["terisi"] as? Boolean ?: false

                                            // Log the data for debugging
                                            Log.d("FirestoreData", "idCCTV: $idCCTV, idParkir: $idParkir, parkirPosition: $parkirPosition, terisi: $terisi")

                                            // Update the parkirPath array based on the status
                                            for (row in parkirPath) {
                                                for (item in row) {
                                                    if (item.id.equals(idParkir, ignoreCase = true) && terisi) {
                                                        item.isOccupied = true
                                                    } else if (item.id.equals(idParkir, ignoreCase = true) && !terisi) {
                                                        item.isOccupied = false
                                                    }

                                                    if (!parkirPosition) { val newMessage = "Terdapat kesalahan parkir pada $idParkir di wilayah $idCCTV"
                                                        addMessage(true, idParkir, newMessage)
                                                    }                                                        }
                                            }

                                            // Update warna parkir berdasarkan kondisi terbaru
                                            warnaParkir[idParkir] = if (terisi) Color.Red else Color.Blue
                                            Log.d("FirestoreData", "idCCTV: $warnaParkir[$idParkir]")

                                            // Hitung slot yang terisi
                                            if (terisi) {
                                                occupiedCount++
                                            }
                                        }
                                        // Hitung slot yang kosong
                                        emptySlots = countPlace - occupiedCount
                                    }
                                } catch (e: Exception) {
                                    Log.e("FirestoreError", "Error fetching data from Firestore", e)
                                }
                                isRefreshing = false
                            }
                        }
                    ) {

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(numberOfColumns),
                            contentPadding = PaddingValues(2.dp)

                        ) {
                            items(parkirPath.flatten()) { item ->
                                Column(
                                    modifier = Modifier
                                        .padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,

                                ) {
                                    val colour = warnaParkir[item.id] ?: Color.Gray

                                    Image(
                                        painter = painterResource(id = R.drawable.kendaraan),
                                        contentDescription = "kendaraan",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        colorFilter = ColorFilter.tint(colour)
                                    )
                                    Text(
                                        text = item.id,
                                        color = colour
                                    )
                                }
                            }
                        }


                    }

                }
            }


        }
    )


    // Notification Dialog
    if (showNotificationDialog) {
        Dialog(onDismissRequest = { showNotificationDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.5f)
                    .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(20.dp))

            ) {
                NotifikasiPage(
                    viewModel,
                    authViewModel,
                    context,
                    pesanList
                )
            }
        }
    }
}

@Composable
fun NotifikasiPage(
    viewModel: FirestoreViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    context: Context,
    pesanList: MutableMap<String, Pair<Boolean, String>>
) {
    val user = authViewModel.auth.currentUser
    val userId = user?.uid
    var photoUrl by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    LaunchedEffect(userId) {
        userId?.let {
            val document = firestore.collection("users").document(it).get().await()
            photoUrl = document.getString("photoUrl") ?: sharedPreferences.getString("photo_url", "") ?: ""
            username = document.getString("name") ?: ""
            email = document.getString("email") ?: ""
            with(sharedPreferences.edit()) {
                putString("photo_url", photoUrl)
                apply()
            }
        }
    }

    // Filter pesan berdasarkan kondisi email
    val filteredMessages = if (email.endsWith("@student.ub.ac.id")) {
        pesanList.filterValues { !it.first }.values.map { it.second } // Hanya pesan dengan key false untuk email student
    } else {
        pesanList.values.map { it.second } // Semua pesan untuk email lainnya
    }

    // Tampilkan pesan yang telah difilter
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(filteredMessages) { message ->
            Row(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()

            ) {
                Text(text = message,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )

            }
            Box( modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White)
                .padding(3.dp))
        }
    }
}


fun pathF1(): Array<Array<ParkingSlot>> {
    val rows = 7
    val columns = 5
    val dummyArray = Array(rows) { row ->
        Array(columns) { col ->
            val rowLabel = ('A' + row).toString()
            val colLabel = (col + 1).toString()
            ParkingSlot(rowLabel + colLabel, false)
        }
    }
    return dummyArray
}

data class ParkingSlot(
    val id: String,
    var isOccupied: Boolean
)

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BerandaPage(
//    modifier: Modifier = Modifier,
//    documentId: String,
//    viewModel: FirestoreViewModel = viewModel(),
//    authViewModel: AuthViewModel = viewModel(),
//    context: Context
//) {
//    var idCCTV by remember { mutableStateOf("") }
//    var idParkir by remember { mutableStateOf("") }
//    var parkirPosition by remember { mutableStateOf(true) }
//    var terisi by remember { mutableStateOf(false) }
//    val warnaParkir = remember { mutableStateMapOf<String, Color>() }
//
//    var pesanList by remember { mutableStateOf(listOf<String>()) }
//    val showNotification by remember { mutableStateOf(false) }
//
//
//
//    val parkirPath = pathF1()
//    val countPlace = parkirPath.size * parkirPath[0].size
//    var emptySlots by remember { mutableStateOf(countPlace) }
//
//    var previousStatus by remember { mutableStateOf(emptySlots > 0) }
//
//    // Check current status
//    val currentStatus = emptySlots > 0
//
//    // refresh
//    val refreshState = rememberPullToRefreshState()
//    var isRefreshing by remember { mutableStateOf(false) }
//    val numberOfColumns = if (parkirPath.isNotEmpty()) parkirPath[0].size else 0
//
//    val coroutineScope = rememberCoroutineScope()
//    var showNotificationDialog by remember { mutableStateOf(false) }
//
//    val user = authViewModel.auth.currentUser
//    val userId = user?.uid
//    var photoUrl by remember { mutableStateOf("") }
//    var username by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//
//    fun addMessage(newMessage: String) {
//        if (newMessage !in pesanList) {
//            pesanList = pesanList + newMessage
//        }
//    }
//
//    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//    LaunchedEffect(userId) {
//        userId?.let {
//            val document = firestore.collection("users").document(it).get().await()
//            photoUrl = document.getString("photoUrl") ?: sharedPreferences.getString("photo_url", "") ?: ""
//            username = document.getString("name") ?: ""
//            email = document.getString("email") ?: ""
//            with(sharedPreferences.edit()) {
//                putString("photo_url", photoUrl)
//                apply()
//            }
//        }
//    }
//
//
//    LaunchedEffect(documentId) {
//        try {
//            viewModel.getAllParkingStatus(documentId) { status ->
//                var occupiedCount = 0
//                status.forEach { (id, data) ->
//                    idCCTV = documentId
//                    idParkir = id
//                    parkirPosition = data["parkirPosition"] as? Boolean ?: false
//                    terisi = data["terisi"] as? Boolean ?: false
//
//                    // Log the data for debugging
//                    Log.d("FirestoreData", "idCCTV: $idCCTV, idParkir: $idParkir, parkirPosition: $parkirPosition, terisi: $terisi")
//
//                    // Update the parkirPath array based on the status
//                    for (row in parkirPath) {
//                        for (item in row) {
//                            if (item.id.equals(idParkir, ignoreCase = true) && terisi) {
//                                item.isOccupied = true
//
//                            } else if (item.id.equals(idParkir, ignoreCase = true) && !terisi) {
//                                item.isOccupied = false
//                            }
//
//                            if (!parkirPosition) {
//                                val newMessage = "Terdapat kesalahan parkir pada $idParkir di wilayah $idCCTV"
//                                addMessage(newMessage)
//                            }
//
//                        }
//                    }
//
//                    // Update warna parkir berdasarkan kondisi terbaru
//                    warnaParkir[idParkir] = if (terisi) Color.Red else Color.Blue
//
//                    // Hitung slot yang terisi
//                    if (terisi) {
//                        occupiedCount++
//                    }
//                }
//                // Hitung slot yang kosong
//                emptySlots = countPlace - occupiedCount
//            }
//        } catch (e: Exception) {
//            Log.e("FirestoreError", "Error fetching data from Firestore", e)
//        }
//    }
//    if (showNotification) {
//        NotifikasiPage( viewModel, authViewModel, context, pesanList,"petugas" ) }
//
//
//    Scaffold(
//
//        topBar = { // Bagian profil pengguna
//            Row( modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 30.dp)
//                .background(
//                    Color.White,
//                    RoundedCornerShape(4.dp)
//                ),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painter = rememberImagePainter(photoUrl),
//                    contentDescription = "profile",
//                    modifier = Modifier
//                        .size(50.dp)
//                        .padding(5.dp)
//                        .clip(CircleShape)
//                        .border(2.dp, Color.Black, CircleShape)
//                )
//                Spacer(modifier = Modifier.width(5.dp))
//                Text(username,modifier = Modifier.padding(2.dp), fontSize = 17.sp,
//                    fontWeight = FontWeight.Bold,
//                    fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
//                    color = Color.Black
//                )
//                Spacer(modifier = Modifier.weight(1f))
//                Image( painter = painterResource(R.drawable.notification_icon),
//                    contentDescription = "notif",
//                    modifier = Modifier
//                        .size(50.dp)
//                        .padding(5.dp)
//                        .clickable {
//                            showNotificationDialog = true
//                        })
//            }
//        },content = { paddingValues -> // Konten Utama
//            Box(modifier = Modifier.padding(paddingValues))
//            {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.LightGray)
//                    .shadow(2.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Top,
//            ) {
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 30.dp),
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        "SLOT TERSISA : ${emptySlots}/$countPlace",
//                        modifier = Modifier
//                            .background(Color.Blue)
//                            .padding(10.dp),
//                        color = Color.White
//                    )
//                    Spacer(modifier = Modifier.width(5.dp))
//                    if (currentStatus != previousStatus) {
//                        if (currentStatus) {
//                            val newMessage = "Parkir kembali kosong"
//                            addMessage(newMessage)
//                        } else {
//                            val newMessage = "Parkir penuh"
//                            addMessage(newMessage)
//                        }
//                        previousStatus = currentStatus
//                    }
//                    if (emptySlots == 0) {
//                        Text(
//                            "Penuh",
//                            modifier = Modifier
//                                .background(Color.Yellow)
//                                .padding(10.dp),
//                            color = Color.Black
//                        )
//                    }
//                }
//
//                PullToRefreshBox(
//                    state = refreshState,
//                    isRefreshing = isRefreshing,
//                    onRefresh = {
//                        coroutineScope.launch {
//                            isRefreshing = true
//                            delay(5000)
//                            try {
//                                viewModel.getAllParkingStatus(documentId) { status ->
//                                    var occupiedCount = 0
//                                    status.forEach { (id, data) ->
//                                        idCCTV = documentId
//                                        idParkir = id
//                                        parkirPosition = data["parkirPosition"] as? Boolean ?: false
//                                        terisi = data["terisi"] as? Boolean ?: false
//
//                                        // Log the data for debugging
//                                        Log.d("FirestoreData", "idCCTV: $idCCTV, idParkir: $idParkir, parkirPosition: $parkirPosition, terisi: $terisi")
//
//                                        // Update the parkirPath array based on the status
//                                        for (row in parkirPath) {
//                                            for (item in row) {
//                                                if (item.id.equals(idParkir, ignoreCase = true) && terisi) {
//                                                    item.isOccupied = true
//                                                } else if (item.id.equals(idParkir, ignoreCase = true) && !terisi) {
//                                                    item.isOccupied = false
//                                                }
//
//                                                if (!parkirPosition) {
//                                                    val newMessage = "Terdapat kesalahan parkir pada $idParkir di wilayah $idCCTV"
//                                                    addMessage(newMessage)
//                                                }
//                                            }
//                                        }
//
//                                        // Update warna parkir berdasarkan kondisi terbaru
//                                        warnaParkir[idParkir] = if (terisi) Color.Red else Color.Blue
//                                        Log.d("FirestoreData", "idCCTV: $warnaParkir[$idParkir]")
//
//                                        // Hitung slot yang terisi
//                                        if (terisi) {
//                                            occupiedCount++
//                                        }
//                                    }
//                                    // Hitung slot yang kosong
//                                    emptySlots = countPlace - occupiedCount
//                                }
//                            } catch (e: Exception) {
//                                Log.e("FirestoreError", "Error fetching data from Firestore", e)
//                            }
//                            isRefreshing = false
//                        }
//                    }
//                ) {
//                    LazyVerticalGrid(
//                        columns = GridCells.Fixed(numberOfColumns), // 5 columns
//                        contentPadding = PaddingValues(8.dp)
//                    ) {
//                        items(parkirPath.flatten()) { item ->
//                            Column(
//                                modifier = Modifier
//                                    .padding(4.dp)
//                                    .background(Color.LightGray)
//                                    .clip(RoundedCornerShape(4.dp))
//                                    .padding(8.dp),
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                val colour = warnaParkir[item.id] ?: Color.Gray
//
//                                Image(
//                                    painter = painterResource(id = R.drawable.kendaraan),
//                                    contentDescription = "kendaraan",
//                                    modifier = Modifier
//                                        .size(30.dp)
//                                        .clip(RoundedCornerShape(4.dp)),
//                                    colorFilter = ColorFilter.tint(colour)
//                                )
//                                Text(
//                                    text = item.id,
//                                    color = colour
//                                )
//                            }
//                        }
//                    }
//                }
//
//            }
//
//            }
//        }
//    )
//
//
//    // Notification Dialog
//    if (showNotificationDialog) {
//        Dialog(onDismissRequest = { showNotificationDialog = false }) {
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight(0.5f)
//                    .background(Color.Black.copy(alpha = 0.5f))
//            ) {
//                NotifikasiPage(
//                    viewModel,
//                    authViewModel,
//                    context, pesanList,
//                    role = if(pesanList.toString().contains("terdapat")) "petugas" else "pengguna"
//                )
//            }
//        }
//    }
//
//
//
//
//}
//
//@Composable
//fun NotifikasiPage(
//    viewModel: FirestoreViewModel = viewModel(),
//    authViewModel: AuthViewModel = viewModel(),
//    context: Context,
//    message: List<String>,
//    role: String ) {
//
//    val user = authViewModel.auth.currentUser
//    val userId = user?.uid
//    var photoUrl by remember { mutableStateOf("") }
//    var username by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//
//    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//    LaunchedEffect(userId) {
//        userId?.let {
//            val document = firestore.collection("users").document(it).get().await()
//            photoUrl = document.getString("photoUrl") ?: sharedPreferences.getString("photo_url", "") ?: ""
//            username = document.getString("name") ?: ""
//            email = document.getString("email") ?: ""
//            with(sharedPreferences.edit()) {
//                putString("photo_url", photoUrl)
//                apply()
//            }
//        }
//    }
//    //salahhh
//
//    if (email.endsWith("@student.ub.ac.id")&& role.equals("pengguna")) {
//        LazyColumn(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            items(message) { message ->
//                Row(
//                    modifier = Modifier
//                        .background(Color.Yellow)
//                        .padding(10.dp)
//                        .fillMaxWidth()
//                ) {
//                    Text(text = message, color = Color.Black)
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//
//    } else
//        LazyColumn(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            items(message) { message ->
//                Row(
//                    modifier = Modifier
//                        .background(Color.Yellow)
//                        .padding(10.dp)
//                        .fillMaxWidth()
//                ) {
//                    Text(text = message, color = Color.Black)
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//}
//
//
//fun pathF1(): Array<Array<ParkingSlot>> {
//    val rows = 7
//    val columns = 5
//    val dummyArray = Array(rows) { row ->
//        Array(columns) { col ->
//            val rowLabel = ('A' + row).toString()
//            val colLabel = (col + 1).toString()
//            ParkingSlot(rowLabel + colLabel, false)
//        }
//    }
//    return dummyArray
//}
//
//data class ParkingSlot(
//    val id: String,
//    var isOccupied: Boolean
//)










