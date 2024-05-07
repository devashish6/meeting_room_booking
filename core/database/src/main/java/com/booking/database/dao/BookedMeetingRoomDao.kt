package com.booking.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.booking.database.model.BookedMeetingRoomEntity

@Dao
interface BookedMeetingRoomDao {
    @Query("SELECT * from meeting_room_booking ORDER BY date ASC, from_time ASC")
    suspend fun getBookedMeetingRooms() : List<BookedMeetingRoomEntity?>
    @Query("SELECT * from meeting_room_booking WHERE date = :date ORDER BY from_time ASC")
    suspend fun getBookingsForDate(date: String) : List<BookedMeetingRoomEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookedMeetingRooms(users: List<BookedMeetingRoomEntity>)

}