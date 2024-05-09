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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MeetingRoomEntity) return false

        if (meetingRoomID != other.meetingRoomID) return false
        if (meetingRoomSize != other.meetingRoomSize) return false
        if (meetingRoomName != other.meetingRoomName) return false
        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = meetingRoomID.hashCode()
        result = 31 * result + meetingRoomSize.hashCode()
        result = 31 * result + meetingRoomName.hashCode()
        result = 31 * result + location.hashCode()
        return result
    }
}
