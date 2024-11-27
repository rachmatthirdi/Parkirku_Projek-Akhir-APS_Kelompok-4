package com.example.parkirku

import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel


import com.google.firebase.auth.FirebaseAuth

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.composable

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(),
    navController: NavController,
    context: Context
) {
    var message by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var resetPasswordMessage by remember { mutableStateOf<String?>(null)}
    val isFormValid by remember {
        derivedStateOf {
            email.isNotBlank() && password.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    var isLoading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_parkirku),
            contentDescription = "Intro Image",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )

        Text("Login",
            modifier = modifier.padding(16.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
            color = colorResource(id = R.color.quaternary_color)
        )

        Spacer(modifier = Modifier.height(50.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    "Please enter a valid email address"
                } else {
                    null
                }
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError != null,
            shape = RoundedCornerShape(30),
            textStyle = TextStyle(color = Color.Black),
            maxLines = 1,
        )
        emailError?.let { errorText ->
            Text(
                text = errorText,
                modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                fontSize = 12.sp,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            shape = RoundedCornerShape(30),
            textStyle = TextStyle(color = Color.Black),
            maxLines = 1
        )
        passwordError?.let { errorText ->
            Text(
                text = errorText,
                modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                fontSize = 12.sp,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "belum punya akun?",
            modifier = Modifier
                .padding(start = 16.dp, top = 4.dp)

                .clickable {
                    navController.navigate("RegisterPage")
                },
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
            color = colorResource(id = R.color.quaternary_color),

        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Lupa Password?",
            modifier = Modifier
                .padding(start = 16.dp, top = 4.dp)
                .clickable {
                    if (email.isNotBlank()) {
                        viewModel.sendPasswordResetEmail(email) { success, resultMessage ->
                            resetPasswordMessage = resultMessage
                        }
                    } else {
                        resetPasswordMessage = "Please enter your email address first."
                    }
                },
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
            color = colorResource(id = R.color.quaternary_color),

        )
        resetPasswordMessage?.let {
            message ->
            Text( text = message,
            color = if (message.contains("sent")) Color.Green else Color.Red,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
            modifier = Modifier.padding(start = 16.dp, top = 4.dp) ) }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                isLoading = true
                viewModel.signIn(email, password, context = context) { success ->
                    isLoading = false
                    isSuccess = success
                    if (isSuccess) {
                        navController.navigate("MainScreen") {
                            popUpTo("LoginPage") { inclusive = true }
                        }
                    } else {
                        passwordError = "Incorrect password. Please try again."
                    }
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors( colorResource(id = R.color.primary_color)),
            shape = RoundedCornerShape(30)
        ) {
            Text("Login",
                color = colorResource(id = R.color.tertiary_color),
                modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                fontSize = 15.sp
            )
        }
        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}

//@Composable
//fun LoginPage(
//    modifier: Modifier = Modifier,
//    viewModel: AuthViewModel = viewModel(),
//    navController: NavController,
//    context: Context
//) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    val isFormValid by remember {
//        derivedStateOf { email.isNotBlank() && password.isNotBlank() }
//    }
//
//    var isLoading by remember { mutableStateOf(false) }
//    var isSuccess by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        TextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                isLoading = true
//                viewModel.signIn(email, password, context = context) { success ->
//                    isLoading = false
//                    isSuccess = success
//                    if (isSuccess) {
//                        navController.navigate("MainScreen") {
//                            popUpTo("LoginPage") { inclusive = true }
//                        }
//                    }
//                }
//            },
//            enabled = isFormValid,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Login")
//        }
//        if (isLoading) {
//            CircularProgressIndicator()
//        }
//    }
//}

//@Composable
//fun LoginPage(
//    modifier: Modifier = Modifier,
//    viewModel: AuthViewModel = viewModel(),
//    navController: NavController,
//    context: Context
//
//) {
//
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    val isFormValid by remember {
//        derivedStateOf { email.isNotBlank() && password.isNotBlank() }
//    }
//
//    var isLoading by remember { mutableStateOf(false) }
//    var isSuccess by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        TextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                isLoading = true
//                viewModel.signIn(email, password, context = context) { success ->
//                    isLoading = false
//                    isSuccess = success
//                }
//                if (isSuccess) {
//                    navController.navigate("MainScreen")
//
//                }
//            },
//            enabled = isFormValid,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Login")
//        }
//        if (isLoading) {
//            CircularProgressIndicator()
//        }
//    }
//
//}


