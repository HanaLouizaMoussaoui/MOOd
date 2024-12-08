package com.example.mood.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mood.R
import com.example.mood.localNavController
import com.example.mood.model.MoodHistory
import com.example.mood.model.User
import com.example.mood.model.UserMood
import com.example.mood.model.enums.MoodTypeEnum
import com.example.mood.ui.NavBar
import com.example.mood.ui.TopBar
import com.example.mood.ui.UiState
import com.example.mood.ui.theme.MOOdTheme
import com.example.mood.viewmodel.MoodViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(contentPadding: PaddingValues, moodViewModel: MoodViewModel) {

    val navController = localNavController.current
    val user =  moodViewModel.currentUser.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    var moodHistory by remember { mutableStateOf(emptyList<MoodHistory>()) }
    var userMoods by remember { mutableStateOf(emptyMap<Int, UserMood>()) }
    val dateTimeFmt = DateTimeFormatter.ofPattern("MMMM dd, yyyy")


    LaunchedEffect(user?.id) {
        if (user != null) {
            coroutineScope.launch {
                val history = moodViewModel.getMoodHistoryFromUserId(user.id)
                moodHistory = history.sortedByDescending { it.dateLogged }.take(3)
                val moods = history.associate { it.userMoodId to moodViewModel.getUserMoodFromMoodId(it.userMoodId) }
                userMoods = moods
            }
        }
    }


    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize())
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

            Spacer(modifier = Modifier.padding(16.dp))

            if (user != null){
                WelcomeBox(user)
            }

            MoodChat(moodViewModel)

            Spacer(modifier = Modifier.padding(16.dp))

            if (moodHistory.isNotEmpty()) {
                MoodHistoryTable(moodHistory, userMoods, dateTimeFmt, moodViewModel)
            } else {
                Text("No Mood Entries", color = MaterialTheme.colorScheme.onSurface)
            }
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
        ){
        AIPromptBox(moodViewModel)
    }
}

@Composable
fun AIPromptBox(moodViewModel: MoodViewModel) {
    val placeholderPrompt = ""
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    val uiState by moodViewModel.uiState.collectAsState()
    val currentPrompt by moodViewModel.currentPrompt.collectAsState()

    Column(modifier = Modifier.height(275.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ){
        Row(
        modifier = Modifier.padding(all = 16.dp)
    ) {
        TextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text("Your message:") },
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
        Box( modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
        ){
            Text(text = currentPrompt ,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

        ){
            Text(
                text = when (uiState) {
                    is UiState.Loading -> "Thinking..."
                    is UiState.Error -> (uiState as UiState.Error).errorMessage
                    is UiState.Success -> (uiState as UiState.Success).outputText
                    else -> stringResource(R.string.results_placeholder)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

@Composable
fun MoodHistoryTable(moodHistory: List<MoodHistory>, userMoods: Map<Int, UserMood>, dateTimeFmt: DateTimeFormatter, moodViewModel: MoodViewModel) {
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(modifier =
        Modifier.padding(8.dp)
            .align(Alignment.CenterHorizontally),text = "Mood History", style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary)


        moodHistory.forEach { mood ->
            val userMood = userMoods[mood.userMoodId]
            val moodType = MoodTypeEnum.values().find { it.id == userMood?.typeId }
            Row(
                modifier = Modifier
                    .padding(8.dp),
            ) {
                Text(
                    text = "You felt ${moodType?.mood} on ${dateTimeFmt.format(mood.dateLogged)} " +
                            "at ${timeFormat.format(mood.dateLogged)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            HorizontalDivider()
        }
    }
}


