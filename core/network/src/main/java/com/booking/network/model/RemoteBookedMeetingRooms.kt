package com.booking.network.model

import com.google.gson.annotations.SerializedName

data class RemoteBookedMeetingRooms(
    @SerializedName("meeting_room_booking_id")
    var meetingRoomBookingID : StringValue,
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
    fun getMeetingRoomBookingID() : String {
        return meetingRoomBookingID.stringValue
    }
    fun getFromTime() : String {
        return fromTime.stringValue
    }
    fun getToTime() : String {
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
        return attendees.arrayValue.values.map {stringValue ->
            stringValue.stringValue
        }
    }

}
