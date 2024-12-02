import android.graphics.Picture
import com.example.mood.model.Mood
import java.time.LocalDateTime

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val createdAt: LocalDateTime,
    val editedAt: LocalDateTime,
    val profilePicture: String?,
    val mood: Mood
)