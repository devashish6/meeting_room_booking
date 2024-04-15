import androidx.room.RoomDatabase
import com.booking.database.dao.MeetingRoomDao
import com.booking.database.dao.UsersDao

abstract class SlotsDataBase : RoomDatabase () {

    abstract fun usersDao() : UsersDao

    abstract fun meetingRooomDao() : MeetingRoomDao

}