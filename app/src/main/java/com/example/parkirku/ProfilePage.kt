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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


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
        if (userId != null) {
            val document = firestore.collection("users").document(userId).get().await()
            photoUrl = document.getString("photoUrl") ?: sharedPreferences.getString("photo_url", "") ?: ""
            username = document.getString("name") ?: ""
            email = document.getString("email") ?: ""
            // Save the photoUrl to SharedPreferences
            with(sharedPreferences.edit()) {
                putString("photo_url", photoUrl)
                apply()
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            val photoUriString = uri.toString()
            photoUrl = photoUriString
            userId?.let { id ->
                viewModel.updateUserPhotoUrl(id, photoUriString)
                with(sharedPreferences.edit()) {
                    putString("photo_url", photoUriString)
                    apply()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Box( modifier = Modifier
                .background(colorResource(id = R.color.quaternary_color))
                .fillMaxWidth()
                .padding(30.dp),
                contentAlignment = Alignment.Center ) {
                Text( text = "Profile",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
                    color = Color.White
                )
            }
        }
    ){
        paddingValues ->
        Box(modifier = Modifier.padding(paddingValues))
        {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(bottom = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {



                Image(
                    painter = if (photoUrl.isNotEmpty()) rememberImagePainter(photoUrl) else painterResource(id = R.drawable.default_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clickable { launcher.launch(arrayOf("image/*")) }
                )


                Column {
                    Text( text = "username",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
                        color = colorResource(id = R.color.quaternary_color)
                    )
                    Box( modifier = Modifier
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                        .padding(8.dp)
                        .fillMaxWidth()
                        ,

                        ) {
                        Text( text = username,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
                            color = colorResource(id = R.color.quaternary_color)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))


                    Text( text = "email",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
                        color = colorResource(id = R.color.quaternary_color),
                        maxLines = 1
                    )
                    Box( modifier = Modifier
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                        .padding(8.dp)
                        .fillMaxWidth()
                        ,

                        ) {
                        Text( text = email,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
                            color = colorResource(id = R.color.quaternary_color),
                            maxLines = 1
                        )
                    }
                }


                Button(onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors( colorResource(id = R.color.primary_color)),
                    shape = RoundedCornerShape(30)
                ) {
                    Text( text = "Logout",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
                        color = colorResource(id = R.color.quaternary_color),
                    )
                }
            }

            if (showLogoutDialog) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(Color.White, shape = RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Logout Confirmation",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
                                color = colorResource(id = R.color.quaternary_color)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Are you sure you want to logout?",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row {
                                Button(colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    onClick = {
                                        showLogoutDialog = false
                                        viewModel.signOut(context) {
                                        }
                                    }
                                ) {
                                    Text("Yes", color = colorResource(id = R.color.quaternary_color))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Button(colors = ButtonDefaults.buttonColors(colorResource(id = R.color.tertiary_color)),
                                    onClick = { showLogoutDialog = false }
                                ) {
                                    Text("No", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

        }

    }

}

