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
)

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
