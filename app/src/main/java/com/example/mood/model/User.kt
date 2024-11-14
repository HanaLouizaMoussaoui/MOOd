import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mood.model.MoodType
import java.time.LocalDateTime

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val createdAt: LocalDateTime,
    val editedAt: LocalDateTime,
    val profilePicture: String?,
    val mood: MoodType?
)