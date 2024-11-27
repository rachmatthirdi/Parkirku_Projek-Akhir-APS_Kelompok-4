package com.example.parkirku


import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import coil.size.Size


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
        Image(
            painter = painterResource(id = R.drawable.logo_parkirku),
            contentDescription = "Intro Image",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )

        Text("Register",
            modifier = modifier.padding(16.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
            color = colorResource(id = R.color.quaternary_color))

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (!email.endsWith("@student.ub.ac.id")) {
                    "Use email Student UB"
                } else {
                    null
                }
            },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            isError = emailError != null,
            shape = RoundedCornerShape(30),
            textStyle = TextStyle(color = Color.Black),
            maxLines = 1,
        )
        emailError?.let { errorText ->
            Text(text = errorText, color = Color.Red,
                modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                fontSize = 12.sp,
                maxLines = 1
            )
        }


        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = password, onValueChange ={
            password = it
            passwordError =
                if (password.length < 6) { "Password must be at least 6 characters" }
                else { null } },
            label = { Text("Password") },
                modifier = Modifier .fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                shape = RoundedCornerShape(30),
                textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
                maxLines = 1,

        )
        passwordError?.let { errorText ->
            Text( text = errorText,
                color = Color.Red,
                modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
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
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmPasswordError != null,
            colors = OutlinedTextFieldDefaults.colors( Color.Black),
            textStyle = TextStyle(color = Color.Black),
            shape = RoundedCornerShape(30),
            maxLines = 1
        )
        confirmPasswordError?.let { errorText ->
            Text(
                text = errorText,
                color = Color.Red,
                modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Sudah punya akun?",
            color = Color.Blue,
            modifier = Modifier
                .padding(start = 16.dp, top = 4.dp)
                .clickable {
                    navController.navigate("LoginPage")
                },
            fontSize = 10.sp,
            maxLines = 1
        )

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
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors( colorResource(id = R.color.primary_color)),
            shape = RoundedCornerShape(30)
        ) {
            Text("Register",
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