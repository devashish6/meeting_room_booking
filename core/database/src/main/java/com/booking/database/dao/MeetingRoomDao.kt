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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMeetingRooms(meetingRooms: List<MeetingRoomEntity>)

}