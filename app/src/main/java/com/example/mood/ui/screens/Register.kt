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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mood.ui.theme.MOOdTheme
import com.example.mood.viewmodel.MoodViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(contentPadding: PaddingValues, moodViewModel: MoodViewModel, navController: NavHostController) {
    MOOdTheme {
        Column(
            modifier = Modifier
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Align items at the top
        ) {
            Register(moodViewModel, navController)
        }
    }
}

@Composable
fun Register(moodViewModel: MoodViewModel, navController: NavHostController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Sign Up
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Email TextField
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {confirmPassword = it},
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Error messages get displayed here
        if (errorMessage != ""){
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Sign Up Button
        Button(
            onClick = {
                // attempts to create a new user
                coroutineScope.launch {
                    val result = createUser(email, password, confirmPassword, moodViewModel)
                    if (result.isEmpty()) {
                        navController.navigate("login") // navigate to the login screen
                    } else {
                        errorMessage = result
                    }
                }},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // Background color
                contentColor = Color.White  // Text color
            ),

            ) {
            Text("Sign Up")
        }

        Button(onClick = {
            navController.navigate("LoginScreenRoute")
        },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan, // Background color
                contentColor = Color.White  // Text color
            ),) {
            Text("Go Login")
        }
    }
}

suspend fun createUser(email: String, password: String, confirmPassword: String, moodViewModel: MoodViewModel): String{
    // checking for no content
    if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
        return "Fields cannot be empty."
    }

    //checking for password mismatch
    if (password != confirmPassword){
        return "Passwords do not match."
    }

    //check if prev. registered
    return if (moodViewModel.checkUserExistsByEmail(email)) {
        "User already exists. Please choose a different email or login."
    } else {
        moodViewModel.createUser(email, password)
        return ""
    }
}