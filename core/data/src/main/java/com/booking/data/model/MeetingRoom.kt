package com.booking.data.model

import com.booking.database.model.MeetingRoomEntity
import com.booking.database.model.UserEntity
import com.booking.network.model.FirebaseDocument
import com.booking.network.model.RemoteMeetingRoom
import com.booking.network.model.RemoteUser

fun RemoteMeetingRoom.asEntity() = MeetingRoomEntity(
    meetingRoomID = getMeetingRoomID(),
    meetingRoomSize = getMeetingRoomSize(),
    location = getLocation()
)

fun FirebaseDocument<RemoteMeetingRoom>.asListOfMeetingRoomEntity(): List<MeetingRoomEntity> {
    return documents.map { firebaseResponseFields ->
        firebaseResponseFields.fields.asEntity()
    }
}
