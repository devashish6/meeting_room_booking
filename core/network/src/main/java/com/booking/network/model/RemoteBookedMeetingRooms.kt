package com.booking.network.model

import com.google.gson.annotations.SerializedName

data class BookedMeetingRoom(
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
    @SerializedName("attendees")
    val attendees: ArrayValue,
)
