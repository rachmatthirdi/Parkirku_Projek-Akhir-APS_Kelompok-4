package com.example.parkirku

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.parkirku.ui.theme.ParkirkuTheme

import kotlinx.coroutines.tasks.await

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(),
    context: Context
) {
    val user = viewModel.auth.currentUser
    val userId = user?.uid
    var photoUrl by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

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

    // Launcher untuk membuka file picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            val photoUriString = uri.toString()
            photoUrl = photoUriString

            // Simpan URI foto dalam Firestore dan SharedPreferences
            userId?.let { id ->
                viewModel.updateUserPhotoUrl(id, photoUriString)
                with(sharedPreferences.edit()) {
                    putString("photo_url", photoUriString)
                    apply()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (photoUrl.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(photoUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clickable {
                        launcher.launch(arrayOf("image/*"))
                    }
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.default_profile),
                contentDescription = "Default Profile Photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clickable {
                        launcher.launch(arrayOf("image/*"))
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Username: $username")
        Text(text = "Email: $email")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            showLogoutDialog = true
        }) {
            Text(text = "Logout")
        }
    }

    // Dialog untuk konfirmasi logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout Confirmation") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.signOut(context) {
                            // Tambahkan logika navigasi ke halaman login setelah logout
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}





//@Composable
//fun ProfilePage(
//    modifier: Modifier = Modifier,
//    viewModel: AuthViewModel = viewModel(),
//    context: Context
//) {
//    val user = viewModel.auth.currentUser
//    val userId = user?.uid
//    var photoUrl by remember { mutableStateOf("") }
//    var username by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
//
//    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//
//    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//
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
//    // Launcher untuk membuka file picker
//    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
//        if (uri != null) {
//            selectedImageUri = uri
//            val photoUriString = uri.toString()
//            photoUrl = photoUriString
//
//            // Simpan URI foto dalam Firestore dan SharedPreferences
//            userId?.let { id ->
//                viewModel.updateUserPhotoUrl(id, photoUriString)
//                with(sharedPreferences.edit()) {
//                    putString("photo_url", photoUriString)
//                    apply()
//                }
//            }
//        }
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        if (photoUrl.isNotEmpty()) {
//            Image(
//                painter = rememberImagePainter(photoUrl),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .border(2.dp, Color.Gray, CircleShape)
//                    .clickable {
//                        launcher.launch(arrayOf("image/*"))
//                    }
//            )
//        } else {
//            Image(
//                painter = painterResource(id = R.drawable.default_profile),
//                contentDescription = "Default Profile Photo",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .border(2.dp, Color.Gray, CircleShape)
//                    .clickable {
//                        launcher.launch(arrayOf("image/*"))
//                    }
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(text = "Username: $username")
//        Text(text = "Email: $email")
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//            viewModel.signOut(context) {
//                // Navigate to LoginPage and pass the photoUrl
//
//            }
//        }) {
//            Text(text = "Logout")
//        }
//    }
//}


//@Composable
//fun ProfilePage(
//    modifier: Modifier = Modifier,
//    viewModel: AuthViewModel = viewModel(),
//    context: Context
//) {
//    val user = viewModel.auth.currentUser
//    val userId = user?.uid
//    var photoUrl by remember { mutableStateOf("") }
//    var username by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
//
//    val firestore = FirebaseFirestore.getInstance()
//    LaunchedEffect(userId) {
//        userId?.let {
//            val document = firestore.collection("users").document(it).get().await()
//            photoUrl = document.getString("photoUrl") ?: ""
//            username = document.getString("name") ?: ""
//            email = document.getString("email") ?: ""
//        }
//    }
//
//    // Launcher untuk membuka file picker
//    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
//        if (uri != null) {
//            selectedImageUri = uri
//            val photoUriString = uri.toString()
//            photoUrl = photoUriString
//
//            // Simpan URI foto dalam Firestore
//            userId?.let { id ->
//                viewModel.updateUserPhotoUrl(id, photoUriString)
//            }
//        }
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        if (photoUrl.isNotEmpty()) {
//            Image(
//                painter = rememberImagePainter(photoUrl),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .border(2.dp, Color.Gray, CircleShape)
//                    .clickable {
//                        launcher.launch(arrayOf("image/*"))
//                    }
//            )
//        } else {
//            Image(
//                painter = painterResource(id = R.drawable.default_profile),
//                contentDescription = "Default Profile Photo",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .border(2.dp, Color.Gray, CircleShape)
//                    .clickable {
//                        launcher.launch(arrayOf("image/*"))
//                    }
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(text = "Username: $username")
//        Text(text = "Email: $email")
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//            viewModel.signOut(context) {
//            // Navigate to LoginPage and pass the photoUrl
//
//            }
//        }) {
//            Text(text = "Logout")
//        }
//    }
//}


//@Composable
//fun ProfilePage(
//    modifier: Modifier = Modifier,
//    viewModel: AuthViewModel = viewModel(),
//    context: Context
//) {
//    val user = viewModel.auth.currentUser
//    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//    val photoPath = sharedPreferences.getString("photo_${user?.email}", "")
//    var username by remember { mutableStateOf("") }
//
//    // Fetch username from Firestore when the composable is first composed
//    LaunchedEffect(user) {
//        if (user != null) {
//            viewModel.getUserInfo(user.uid) { result ->
//                result?.let {
//                    username = it["name"] ?: ""
//                }
//            }
//        }
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        // Foto profil
//        if (!photoPath.isNullOrEmpty()) {
//            Image(
//                painter = rememberImagePainter(photoPath),
//                contentDescription = "Profile Photo",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .background(Color.Gray)
//            )
//        } else {
//            Image(
//                painter = painterResource(id = R.drawable.default_profile),
//                contentDescription = "Default Profile Photo",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .background(Color.Gray)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Kolom username
//        Text(text = username)
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Kolom email
//        Text(text = user?.email ?: "Email")
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Tombol logout
//        Button(onClick = {
//            viewModel.signOut(context) {
//            }
//        }) {
//            Text(text = "Logout")
//        }
//    }
//}
