package com.example.parkirku

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
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
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_parkirku), // Replace with your drawable resource
                contentDescription = "Intro Image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)

            )
            Row {
                Text(
                    text = "PARKIR",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp),
                    color = colorResource(id = R.color.quaternary_color)
                )
                Text(
                    text = "KU",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp),
                    color = colorResource(id = R.color.secondary_color)
                )
            }


        }
        Box (modifier = Modifier.weight(1f)){
            Image(painter = painterResource(id = R.drawable.bg_intro) ,
                contentDescription ="bg",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
                )

            Column( modifier = Modifier.fillMaxSize() ) {
                Spacer(modifier = Modifier.weight(0.6f))
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .weight(0.4f) ) {
                    Button( onClick = {
                        navController.navigate("LoginPage") },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors( colorResource(id = R.color.secondary_color))
                    ) {
                        Text(text = "Login",
                            color = colorResource(id = R.color.tertiary_color),
                            modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                            fontSize = 15.sp)
                    }
                    Button( onClick = { navController.navigate("RegisterPage")
                                      },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors( colorResource(id = R.color.quaternary_color))
                    ) {
                        Text("Register",
                            color = Color.White,
                            modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

    }
}
