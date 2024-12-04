package com.example.mood.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mood.R
import com.example.mood.localNavController
import com.example.mood.model.User
import com.example.mood.ui.NavBar
import com.example.mood.ui.TopBar
import com.example.mood.ui.theme.MOOdTheme
import com.example.mood.viewmodel.MoodViewModel

@Composable
fun HomeScreen(contentPadding: PaddingValues, moodViewModel: MoodViewModel) {

    val navController = localNavController.current
    val user =  moodViewModel.currentUser.collectAsState().value

    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background))
    {
        Column(
            modifier = Modifier
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TopBar { navController.navigate("UserAccount") }
            NavBar(
                onHomeClick = { navController.navigate("HomeScreen") },
                onLogClick = { navController.navigate("LogMood") }
            )

            if (user != null){
                WelcomeBox(user)
            }

            MoodChat(moodViewModel)
        }
    }
}

@Composable
fun WelcomeBox(user: User){

    Image(
       painterResource(id = com.example.mood.R.drawable.mood),
        contentDescription = "Home icon"
    )
    Text(
        text = "Hi there, ${user.name}!",
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier.padding(bottom = 24.dp),
        color = MaterialTheme.colorScheme.primary
    )
}


@Composable
fun MoodChat(moodViewModel: MoodViewModel){
    Column(        modifier = Modifier
        .fillMaxSize()){
        AIPromptBox(moodViewModel)
    }
}

@Composable
fun AIPromptBox(moodViewModel: MoodViewModel) {
    val placeholderPrompt = stringResource(R.string.prompt_placeholder)
    val placeholderResult = stringResource(R.string.results_placeholder)
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val results by moodViewModel.results.collectAsState()
    val uiState by moodViewModel.uiState.collectAsState()

    Column(modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,

    ){
        LazyColumn {
            items(results) { resultItem ->
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    TextField(
                        value = resultItem.result
                            ?: if (resultItem.isLoading) "Thinking..." else "No response",
                        onValueChange = {},
                        enabled = false, // Make result fields read-only
                        label = { Text("Result for: ${resultItem.prompt}") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        Row(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            TextField(
                value = prompt,
                onValueChange = { prompt = it },
                modifier = Modifier
                    .weight(0.8f)
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically)
            )

            Button(
                onClick = {
                    moodViewModel.sendPrompt(prompt)
                },
                enabled = prompt.isNotEmpty(),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = "Send")
            }
        }
    }
}

