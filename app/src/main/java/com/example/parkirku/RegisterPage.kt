package com.example.parkirku


import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


import com.google.firebase.auth.FirebaseAuth


@Composable
fun RegisterPage(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(),
    navController: NavController,
    context: Context
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val isFormValid by remember {
        derivedStateOf {
            email.isNotBlank() &&
                    password.isNotBlank() &&
                    password == confirmPassword &&
                    email.endsWith("@student.ub.ac.id") &&
                    password.length >= 6
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
        Text("Register", modifier = modifier.padding(16.dp))

        TextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (!email.endsWith("@student.ub.ac.id")) {
                    "Email must end with @student.ub.ac.id"
                } else {
                    null
                }
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError != null
        )
        emailError?.let { errorText ->
            Text(text = errorText, color = Color.Red, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = if (password.length < 6) {
                    "Password must be at least 6 characters"
                } else {
                    null
                }
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null
        )
        passwordError?.let { errorText ->
            Text(text = errorText, color = Color.Red, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = if (confirmPassword != password) {
                    "Passwords do not match"
                } else {
                    null
                }
            },
            label = { Text("Confirmation Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmPasswordError != null
        )
        confirmPasswordError?.let { errorText ->
            Text(text = errorText, color = Color.Red, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                viewModel.signUp(email, password, photoUrl = "null", context = context) { success ->
                    isLoading = false
                    isSuccess = success
                    if (isSuccess) {
                        navController.navigate("LoginPage")
                    }
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}

//@Composable
//fun RegisterPage(
//    modifier: Modifier = Modifier,
//    viewModel: AuthViewModel = viewModel(),
//    navController: NavController,
//    context: Context
//
//) {
//
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    val isFormValid by remember {
//        derivedStateOf {
//            email.isNotBlank() &&
//                    password.isNotBlank() &&
//                    password == confirmPassword
//        }
//    }
//
//    var isLoading by remember { mutableStateOf(false) }
//    var isSuccess by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Register", modifier = modifier.padding(16.dp))
//
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
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = confirmPassword,
//            onValueChange = { confirmPassword = it },
//            label = { Text("Confirmation Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("Register",
//            modifier = modifier.padding(16.dp)
//                .clickable {
//                    navController.navigate("LoginPage")
//                })
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                isLoading = true
//                viewModel.signUp(email, password, photoUrl="null", context=context) { success ->
//                    isLoading = false
//                    isSuccess = success
//
//                }
//
//                if (isSuccess) {
//                    navController.navigate("LoginPage")
//
//                }
//            },
//            enabled = isFormValid,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Register")
//        }
//        if (isLoading) {
//        CircularProgressIndicator()
//    }
//    }
//
//
//}


//fun registerUser(email: String, password: String, onResult: (Boolean) -> Unit) {
//    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
//        .addOnCompleteListener { task ->
//            onResult(task.isSuccessful)
//        }
//}