package com.example.mood.ui
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mood.R


@Composable
fun TopBar(
    onHomeClick: () -> Unit
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = Color.Black,
        modifier = Modifier.height(50.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "My MOOd",
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center)
            )

            IconButton(
                onClick = onHomeClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Account icon",
                    tint = Color.Black
                )
            }
        }
    }


}
