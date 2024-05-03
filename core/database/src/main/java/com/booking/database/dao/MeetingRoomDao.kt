package com.booking.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.booking.database.model.MeetingRoomEntity

@Dao
interface MeetingRoomDao {
    @Query("SELECT * from meeting_room")
    suspend fun getAllMeetingRooms() : List<MeetingRoomEntity?>

    @Query("SELECT * FROM meeting_room WHERE meeting_room_id = :key")
    suspend fun getMeetingRoomByID(key: String) : MeetingRoomEntity?

    @Query("SELECT * FROM meeting_room WHERE meeting_room_id NOT IN (SELECT meeting_room_id FROM meeting_room_booking WHERE date = :date AND ((from_time >= :fromTime AND from_time < :toTime) OR (toTime >= :fromTime AND toTime < :toTime)))")
    suspend fun getAvailableMeetingRooms(date: String, fromTime: String, toTime: String): List<MeetingRoomEntity?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMeetingRooms(meetingRooms: List<MeetingRoomEntity>)

}