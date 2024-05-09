package com.booking.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "meeting_room_booking")
data class BookedMeetingRoomEntity(
    @PrimaryKey @ColumnInfo(name = "meeting_room_booking_id") var meetingRoomBookID: String,
    @ColumnInfo(name = "meeting_room_id") var meetingRoomID: String,
    @ColumnInfo(name = "from_time") var fromTime: String,
    @ColumnInfo(name = "toTime") var toTime: String,
    @ColumnInfo(name = "host") var host: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "meeting_title") var meetingTitle: String,
    @ColumnInfo(name = "attendees") var attendees: List<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookedMeetingRoomEntity

        if (meetingRoomBookID != other.meetingRoomBookID) return false
        if (meetingRoomID != other.meetingRoomID) return false
        if (fromTime != other.fromTime) return false
        if (toTime != other.toTime) return false
        if (host != other.host) return false
        if (date != other.date) return false
        if (meetingTitle != other.meetingTitle) return false
        if (attendees != other.attendees) return false

        return true
    }

    override fun hashCode(): Int {
        var result = meetingRoomBookID.hashCode()
        result = 31 * result + meetingRoomID.hashCode()
        result = 31 * result + fromTime.hashCode()
        result = 31 * result + toTime.hashCode()
        result = 31 * result + host.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + meetingTitle.hashCode()
        result = 31 * result + attendees.hashCode()
        return result
    }
}

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}
