package com.booking.network.model

import com.google.gson.annotations.SerializedName

data class MeetingRoom(
    @SerializedName("meeting_room_id")
    val meetingRoomId: StringValue,
    @SerializedName("meeting_room_size")
    val meetingRoomSize: IntegerValue,
    @SerializedName("location")
    val location: StringValue
)

