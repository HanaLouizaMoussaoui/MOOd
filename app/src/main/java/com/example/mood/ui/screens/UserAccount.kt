package com.example.mood.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mood.localNavController
import com.example.mood.ui.NavBar
import com.example.mood.ui.TopBar
import com.example.mood.viewmodel.MoodViewModel
import kotlinx.coroutines.launch
import com.example.mood.model.MoodHistory
import java.time.format.DateTimeFormatter


@Composable
fun UserAccountScreen(contentPadding: PaddingValues, moodViewModel: MoodViewModel, onThemeSelected: (String) -> Unit) {
    val navController = localNavController.current
    if (moodViewModel.currentUser.collectAsState().value == null) {
        navController.navigate("LoginScreenRoute")
    }
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
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
            UserAccount(onThemeSelected, moodViewModel)
        }

    }
}

@Composable
fun UserAccount(onThemeSelected: (String) -> Unit, moodViewModel: MoodViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val user =  moodViewModel.currentUser.collectAsState().value
    var moodHistory by remember { mutableStateOf(emptyList<MoodHistory>()) }

    // Edit-able fields for the user
    var isEditing by remember { mutableStateOf(false) }
    var editableName by remember { mutableStateOf(user?.name ?: "") }
    var editableEmail by remember { mutableStateOf(user?.email ?: "") }
    var editablePassword by remember { mutableStateOf(user?.password ?: "") }
    var editableTheme by remember { mutableStateOf(user?.colourTheme ?: "Light") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (user != null) {
            val dateTimeFmt = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm")

            Icon(
                painter = painterResource(id = com.example.mood.R.drawable.user),
                contentDescription = "Account icon",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(100.dp)
            )

            OutlinedTextField(
                value = editableName,
                onValueChange = { editableName = it },
                label = {Text ("Name")},
                modifier = Modifier.padding(bottom = 24.dp),
                enabled = isEditing,
            )

            OutlinedTextField(
                value = editableEmail,
                onValueChange = { editableEmail = it },
                label = {Text ("Email")},
                enabled = isEditing,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            if (isEditing) {
                Text("Select Theme", color = MaterialTheme.colorScheme.onSurface)
                SelectionBar(
                    selectedTheme = editableTheme,
                    onThemeSelected,
                    onThemeSelectedDatabase = { theme ->
                        editableTheme = theme
                    }
                )
            } else {
                Text(if (editableTheme != "") "Current theme: $editableTheme" else "Current theme: Default", color = MaterialTheme.colorScheme.onSurface)

            }

            Button(
                onClick = {
                    if (isEditing) {
                        coroutineScope.launch {
                            // Save changes
                            moodViewModel.updateUserInfo(
                                user,
                                editableName,
                                editableEmail,
                                editablePassword,
                                editableTheme
                            )
                        }
                }
                    // Toggle edit mode
                    isEditing = !isEditing },
                modifier = Modifier.fillMaxWidth(),
                // making sure the user has entered a proper name and email
                enabled = editableName.isNotEmpty() && editableEmail.isNotEmpty() && editablePassword.isNotEmpty()
            ) {
                Text(if (isEditing) "Save Changes" else "Edit")
            }

            // Password TextField
            OutlinedTextField(
                value = editablePassword,
                onValueChange = {editablePassword = it},
                enabled = isEditing,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    moodHistory = moodViewModel.getMoodHistoryFromUserId(user.id)
                    moodHistory.sortedByDescending { it.dateLogged }

                }
            }

            OutlinedTextField(
                value = moodHistory.firstOrNull()?.dateLogged?.let { dateTimeFmt.format(it) } ?: "No Mood Entries",
                onValueChange = {},
                enabled = false,
                label = { Text("First Mood Entry") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = moodHistory.lastOrNull()?.dateLogged?.let { dateTimeFmt.format(it) } ?: "No Mood Entries",
                onValueChange = {},
                enabled = false,
                label = { Text("Latest Mood Entry") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = user.editedAt.let { dateTimeFmt.format(it) } ?: "No Edits Yet",
                onValueChange = {},
                enabled = false,
                label = { Text("Last Edited") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
        else {
            Text("No user data found. Please exit and login.")
        }
    }
}

@Composable
fun SelectionBar(selectedTheme: String, onThemeSelected: (String) -> Unit, onThemeSelectedDatabase: (String) -> Unit) {
    val tabs = listOf("Light", "Default", "Dark")
    var selectedTabIndex = remember(selectedTheme) {
        tabs.indexOf(selectedTheme).takeIf { it >= 0 } ?: 1
    }

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index
                    onThemeSelectedDatabase(title)
                    onThemeSelected(title) },
                text = { Text(text = title, color= MaterialTheme.colorScheme.onSurface) }
            )
        }
    }
}

