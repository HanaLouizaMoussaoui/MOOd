package com.example.mood

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.mood.data.DatabaseProvider
import com.example.mood.data.MoodRepository
import java.time.LocalDate
import java.time.YearMonth
import com.example.mood.model.MoodType
import com.example.mood.model.MoodHistory
import com.example.mood.model.User
import kotlinx.coroutines.launch
import java.time.LocalDateTime.now


class MainActivity : ComponentActivity() {
    private val moodRepository: MoodRepository by lazy {
        val database = DatabaseProvider.AppDatabase.getDatabase(this)
        MoodRepository(database.userDao())
    }
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sampleMoodLogs = listOf(
                MoodHistory(1, 1, MoodType.HAPPY, LocalDate.of(2024, 11, 1)),
                MoodHistory(2, 1, MoodType.SAD, LocalDate.of(2024, 11, 2)),
                MoodHistory(3, 1, MoodType.NEUTRAL, LocalDate.of(2024, 11, 3)),
                MoodHistory(4, 1, MoodType.ANXIOUS, LocalDate.of(2024, 11, 4)),
                MoodHistory(5, 1, MoodType.HAPPY, LocalDate.of(2024, 11, 5)),
                MoodHistory(6, 1, MoodType.ANGRY, LocalDate.of(2024, 11, 6)),
            )
            var user: User? = null
            Box(modifier = Modifier.fillMaxSize()) {
                Button(
                    onClick = {
                        user = getUserById(2)
                    }, modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text("Add User")
                }
                MoodCalendar(monthLogs = sampleMoodLogs, user = user)
            }
        }
    }

        fun showPopup(context: Context, title: String, message: String) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        fun addUser() {
            lifecycleScope.launch {
                moodRepository.addUser(
                    name = "Ryan", password = "password", email = "email@email.com",
                    createdAt = now(), editedAt = now(), profilePicture = null, mood = null
                )
            }
        }

    private fun getUserById(id: Int) : User? {
        var user: User? = null
        lifecycleScope.launch {
           user = moodRepository.getUserById(id)
        }
        return user
    }
}




@SuppressLint("NewApi")
@Composable
fun MoodCalendar(monthLogs: List<MoodHistory>, user: User?) {
    val currentMonth = YearMonth.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val datesInMonth = (1..daysInMonth).map { currentMonth.atDay(it) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(datesInMonth) { date ->
            val moodForDay = monthLogs.find { it.dateLogged == date }?.mood
            MoodDayItem(date, moodForDay)
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun MoodDayItem(date: LocalDate, mood: MoodType?) {
    val moodColor = when (mood) {
        MoodType.HAPPY -> Color.Green
        MoodType.SAD -> Color.Blue
        MoodType.NEUTRAL -> Color.Gray
        MoodType.ANGRY -> Color.Red
        MoodType.ANXIOUS -> Color.Yellow
        null -> Color.LightGray
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .background(moodColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyLarge
        )
    }

}

@SuppressLint("NewApi")
@Composable
fun PreviewMoodCalendar() {
    val sampleMoodLogs = listOf(
        MoodHistory(1, 1, MoodType.HAPPY, LocalDate.of(2024, 11, 1)),
        MoodHistory(2, 1, MoodType.SAD, LocalDate.of(2024, 11, 2)),
        MoodHistory(3, 1,MoodType.NEUTRAL, LocalDate.of(2024, 11, 3)),
        MoodHistory(4,1,MoodType.ANXIOUS, LocalDate.of(2024, 11, 4)),
        MoodHistory(5,1, MoodType.HAPPY, LocalDate.of(2024, 11, 5)),
        MoodHistory(6,1, MoodType.ANGRY, LocalDate.of(2024, 11, 6)),
    )
    MoodCalendar(monthLogs = sampleMoodLogs, null)
}

@Preview(showBackground = true)
@Composable
fun MoodCalendarPreview() {
    PreviewMoodCalendar()
}
