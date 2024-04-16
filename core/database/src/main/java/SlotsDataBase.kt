import androidx.room.Database
import androidx.room.RoomDatabase
import com.booking.database.dao.MeetingRoomDao
import com.booking.database.dao.UsersDao
import com.booking.database.model.MeetingRoomEntity
import com.booking.database.model.UserEntity

@Database(entities = [MeetingRoomEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class SlotsDataBase : RoomDatabase () {

    abstract fun usersDao() : UsersDao

    abstract fun meetingRooomDao() : MeetingRoomDao

}