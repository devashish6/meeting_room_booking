package com.booking.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.booking.database.dao.MeetingRoomDao
import com.booking.database.dao.BookedMeetingRoomDao
import com.booking.database.dao.UsersDao
import com.booking.database.model.MeetingRoomEntity
import com.booking.database.model.UserEntity
import com.booking.database.model.BookedMeetingRoomEntity

@Database(
    entities = [MeetingRoomEntity::class, UserEntity::class, BookedMeetingRoomEntity::class],
    version = 1,
    exportSchema = false)
abstract class SlotsDataBase : RoomDatabase () {

    abstract fun usersDao() : UsersDao

    abstract fun meetingRooomDao() : MeetingRoomDao

    abstract fun userMeeting() : BookedMeetingRoomDao

}