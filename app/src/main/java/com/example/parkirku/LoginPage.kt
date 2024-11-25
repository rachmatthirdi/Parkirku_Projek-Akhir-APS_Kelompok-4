package com.example.parkirku

import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
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
            isError = emailError != null
        )
        emailError?.let { errorText ->
            Text(
                text = errorText,
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null
        )
        passwordError?.let { errorText ->
            Text(
                text = errorText,
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "belum punya akun?",
            color = Color.Blue,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                .clickable {
                    navController.navigate("RegisterPage")
                }

        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Lupa Password?",
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                .clickable{
                    if (email.isNotBlank()) {
                        viewModel.sendPasswordResetEmail(email) { success, resultMessage ->resetPasswordMessage = resultMessage } }
                    else { resetPasswordMessage = "Please enter your email address first." }
                }

        )
        resetPasswordMessage?.let {
            message -> Text( text = message, color = if (message.contains("sent")) Color.Green
        else Color.Red, modifier = Modifier.padding(start = 16.dp, top = 4.dp) ) }
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
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


