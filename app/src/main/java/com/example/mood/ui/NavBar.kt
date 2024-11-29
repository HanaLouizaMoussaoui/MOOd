package com.example.mood.ui
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@Composable
fun NavBar(
    onHomeClick: () -> Unit
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = Color.Black,
        modifier = Modifier.height(50.dp)
    ) {
        Column {
            IconButton(onClick = onHomeClick,
                modifier = Modifier.height(20.dp)) {
                // Icon(
                // painter = painterResource(id = R.drawable.ic_home),
                //  contentDescription = stringResource(id = R.string.home),
                //  tint = MaterialTheme.colorScheme.onPrimary
                // )

            }
            Text("Dashboard")
        }
        Spacer(modifier = Modifier.width(190.dp))
        Column {
            IconButton(onClick = onHomeClick,
                modifier = Modifier.height(20.dp)) {
                // Icon(
                // painter = painterResource(id = R.drawable.ic_home),
                //  contentDescription = stringResource(id = R.string.home),
                //  tint = MaterialTheme.colorScheme.onPrimary
                // )

            }
            Text("Log New Mood")
        }

    }
}
