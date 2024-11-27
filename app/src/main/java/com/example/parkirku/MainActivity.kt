package com.example.parkirku

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.parkirku.ui.theme.ParkirkuTheme
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.parkirku.LoginPage
import com.example.parkirku.RegisterPage
import com.example.parkirku.MainScreen

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parkirku.ui.theme.Typography
import com.google.firebase.FirebaseApp
import okhttp3.internal.wait

val customTypography = androidx.compose.material3.Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.dm_sans_thin)),
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp )
)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ParkirkuTheme {


                FirebaseApp.initializeApp(this)
                val navController: NavHostController = rememberNavController()
                val context = LocalContext.current
                val sharedPreferences =
                    context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn) {
                        navController.navigate("MainScreen") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                }

                MaterialTheme(
                    colorScheme = MaterialTheme.colorScheme.copy(
                        background = Color.White
                    ),
                    typography = customTypography

                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ){
                        NavHost(navController = navController, startDestination = "IntroPage") {
                            composable("IntroPage") { IntroPage(navController) }
                            composable("LoginPage") { LoginPage(
                                modifier = Modifier,
                                viewModel = viewModel(),
                                navController,
                                context = LocalContext.current

                            ) }
                            composable("RegisterPage") { RegisterPage(
                                modifier = Modifier,
                                viewModel = viewModel(),
                                navController,
                                context = LocalContext.current

                            ) }
                            composable("MainScreen") { MainScreen() }

                        }
                    }
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkirkuTheme {
        val navController = rememberNavController() //
        IntroPage(navController = navController)
    }
}