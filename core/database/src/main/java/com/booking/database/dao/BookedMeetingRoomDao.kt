package com.booking.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.booking.database.model.BookedMeetingRoomEntity
import com.booking.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookedMeetingRoomDao {
    @Query("SELECT * from meeting_room_booking")
    suspend fun getBookedMeetingRooms() : List<BookedMeetingRoomEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookedMeetingRooms(users: List<BookedMeetingRoomEntity>)

}