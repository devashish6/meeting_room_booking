package com.booking.data.model

import com.booking.database.model.MeetingRoomEntity
import com.booking.database.model.UserEntity
import com.booking.model.model.MeetingRoom
import com.booking.network.model.FirebaseDocument
import com.booking.network.model.RemoteMeetingRoom
import com.booking.network.model.RemoteUser

fun RemoteMeetingRoom.asEntity() = MeetingRoomEntity(
    meetingRoomName = getMeetingRoomName(),
    meetingRoomID = getMeetingRoomID(),
    meetingRoomSize = getMeetingRoomSize(),
    location = getLocation()
)
fun MeetingRoomEntity.asDomainModel() = MeetingRoom(
    meetingRoomName = meetingRoomName,
    meetingRoomID = meetingRoomID,
    meetingRoomSize = meetingRoomSize,
    location = location
)

fun FirebaseDocument<RemoteMeetingRoom>.asListOfMeetingRoomEntity(): List<MeetingRoomEntity> {
    return documents.map { firebaseResponseFields ->
        firebaseResponseFields.fields.asEntity()
    }
}
