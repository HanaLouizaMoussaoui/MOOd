package com.example.mood.ui
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mood.R


@Composable
fun NavBar(
    onHomeClick: () -> Unit
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = Color.Black,
        modifier = Modifier.height(70.dp)
            .fillMaxWidth(),

    ) {
        Column  (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 40.dp )
        ){
            IconButton(onClick = onHomeClick,
                modifier = Modifier.height(20.dp)) {
                Icon(
                painter = painterResource(id = R.drawable.home),

                 contentDescription = "Home icon",
                 tint = Color.Black
                 )

            }
            Text("Dashboard")
        }
        Spacer(modifier = Modifier.width(120.dp))
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            IconButton(onClick = onHomeClick,
                modifier = Modifier.height(20.dp)) {
                 Icon(
                 painter = painterResource(id = R.drawable.bookmark),
                  contentDescription = "hello",
                 tint = Color.Black
                 )

            }
            Text("Log a Mood")
        }

    }
}
