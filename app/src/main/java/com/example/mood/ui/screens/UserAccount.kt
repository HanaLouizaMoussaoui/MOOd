package com.example.mood.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mood.ui.NavBar


@Composable
fun UserAccountScreen(contentPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // Align items at the top
    ) {
        NavBar { }
        UserAccount()
    }
}



@Preview(showBackground = true)
@Composable
fun UserAccountPreview() {
    UserAccountScreen(PaddingValues(8.dp))
}

@Composable
fun UserAccount() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Username",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Text(
            text = "email@email.com",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Text(
            text = "Theme",
            style = MaterialTheme.typography.bodyMedium
        )
        SelectionBar()

        Button(
            onClick = { /* Handle Edit */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Edit")
        }
        // Email TextField
        OutlinedTextField(
            value = "xxxx",
            onValueChange = {},
            readOnly = true,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "October 30th 2024",
            onValueChange = {},
            readOnly = true,
            label = { Text("First Mood Entry") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "November 28th 2024",
            onValueChange = {},
            readOnly = true,
            label = { Text("Latest Mood Entry") },
            modifier = Modifier.fillMaxWidth(),
        )
    }


}

@Composable
fun SelectionBar() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Light", "Default", "Dark")

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = { BasicText(text = title) }
            )
        }
    }
}

