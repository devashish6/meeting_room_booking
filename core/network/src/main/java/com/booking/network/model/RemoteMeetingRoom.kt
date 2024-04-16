package com.booking.network.model

import com.google.gson.annotations.SerializedName

data class RemoteMeetingRoom(
    @SerializedName("meeting_room_id")
    val meetingRoomId: StringValue,
    @SerializedName("meeting_room_size")
    val meetingRoomSize: IntegerValue,
    @SerializedName("location")
    val location: StringValue
) {
    fun getMeetingRoomID() : String {
        return meetingRoomId.stringValue
    }
    fun setMeetingRoomID(encryptedID: String) {
        meetingRoomId.stringValue = encryptedID
    }
    fun getMeetingRoomSize() : String {
        return meetingRoomSize.integerValue
    }
    fun getLocation() : String {
        return location.stringValue
    }
}

