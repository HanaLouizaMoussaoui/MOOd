package com.example.mood.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mood.data.repositories.UserRepository
import com.example.mood.ui.theme.MOOdTheme
import com.example.mood.viewmodel.MoodViewModel
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(contentPadding: PaddingValues, moodViewModel: MoodViewModel,  navController: NavHostController) {
    MOOdTheme {
        Column(
            modifier = Modifier
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Align items at the top
        ) {
            Login(navController)
        }
    }
}

@Composable
fun Login(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(id = com.example.mood.R.drawable.ic_launcher_foreground),

            contentDescription = "Home icon",
            tint = Color.Black
        )
        Text(
            text = "Login",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Email TextField
        OutlinedTextField(
            value = "email",
            onValueChange = {email = it},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        OutlinedTextField(
            value = "password",
            onValueChange = {password = it},
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { loginUser() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // Background color
                contentColor = Color.White  // Text color
            )
        ) {
            Text("Login")
        }

        Button(onClick = {
            navController.navigate("Register")
        },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan, // Background color
                contentColor = Color.White  // Text color
            ),) {
            Text("Go Register")
        }
    }
}

fun loginUser(){

}

