package com.booking.network.model

import com.google.gson.annotations.SerializedName

data class MeetingRooms(
    @SerializedName("documents")
    val documents: List<MeetingRoom>
)

data class MeetingRoom(
    @SerializedName("name")
    val name: String,
    @SerializedName("fields")
    val fields: MeetingRoomFields,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("updateTime")
    val updateTime: String
)

data class MeetingRoomFields(
    @SerializedName("meeting_room_id")
    val meetingRoomId: MeetingRoomId,
    @SerializedName("meeting_room_size")
    val meetingRoomSize: MeetingRoomSize,
    @SerializedName("location")
    val location: Location
)

data class MeetingRoomId(
    @SerializedName("stringValue")
    val stringValue: String
)

data class MeetingRoomSize(
    @SerializedName("integerValue")
    val integerValue: String
)

data class Location(
    @SerializedName("stringValue")
    val stringValue: String
)
