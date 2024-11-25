package com.example.parkirku

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun IntroPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display gambar
        Image(
            painter = painterResource(id = R.drawable.your_image), // Replace with your drawable resource
            contentDescription = "Intro Image",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )

        // Display text
        Text(
            text = "Welcome to the App!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color.Black
        )

        // Display Login Button
        Button(
            onClick = { navController.navigate("LoginPage") }, // Navigate to Login Page
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Login")
        }

        // Display Register Button
        Button(
            onClick = { navController.navigate("RegisterPage") }, // Navigate to Register Page
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Register")
        }
    }
}
