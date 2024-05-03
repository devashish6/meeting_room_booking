package com.booking.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meeting_room")
data class MeetingRoomEntity(
    @PrimaryKey @ColumnInfo(name = "meeting_room_id") val meetingRoomID : String,
    @ColumnInfo(name = "meeting_room_size") val meetingRoomSize : String,
    @ColumnInfo(name = "meeting_room_name") val meetingRoomName : String,
    @ColumnInfo(name = "meeting_room_location") val location : String
)
