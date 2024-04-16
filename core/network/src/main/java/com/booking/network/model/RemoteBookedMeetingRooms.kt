package com.booking.network.model

import com.google.gson.annotations.SerializedName

data class BookedMeetingRoom(
    var meetingRoomBookingID : String,
    @SerializedName("from_time")
    val fromTime: StringValue,
    @SerializedName("to_time")
    val toTime: StringValue,
    @SerializedName("meeting_room_id")
    val meetingRoomId: StringValue,
    @SerializedName("host")
    val host: StringValue,
    @SerializedName("meeting_title")
    val meetingTitle: StringValue,
    @SerializedName("date")
    val date: StringValue,
    @SerializedName("attendees")
    val attendees: ArrayValue,
) {
    fun getFromTime() : String {
        return fromTime.stringValue
    }
    fun getToTine() : String {
        return toTime.stringValue
    }
    fun getMeetingRoomID() : String {
        return meetingRoomId.stringValue
    }
    fun getHost() : String {
        return host.stringValue
    }
    fun getMeetingTitle() : String {
        return meetingTitle.stringValue
    }
    fun getDate() : String {
        return date.stringValue
    }
    fun getAttendees() : List<String> {
        return attendees.arrayValue
    }

}
