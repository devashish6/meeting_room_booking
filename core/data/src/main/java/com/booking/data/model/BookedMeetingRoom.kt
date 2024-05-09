package com.booking.data.model

import com.booking.database.model.BookedMeetingRoomEntity
import com.booking.model.model.BookedMeetingRoom
import com.booking.network.model.FirebaseDocument
import com.booking.network.model.RemoteBookedMeetingRooms
import java.util.UUID

fun RemoteBookedMeetingRooms.asEntity() = BookedMeetingRoomEntity(
    meetingRoomBookID = getMeetingRoomBookingID(),
    meetingRoomID = getMeetingRoomID(),
    fromTime = getFromTime(),
    toTime = getToTime(),
    host = getHost(),
    date = getDate(),
    meetingTitle = getMeetingTitle(),
    attendees = getAttendees()
)
fun BookedMeetingRoomEntity.asDomainModel() = BookedMeetingRoom(
    meetingRoomBookID = meetingRoomBookID,
    meetingRoomID = meetingRoomID,
    fromTime = fromTime,
    toTime = toTime,
    host = host,
    date = date,
    meetingTitle = meetingTitle,
    attendees = attendees
)


fun FirebaseDocument<RemoteBookedMeetingRooms>.asListOfBookedMeetingRoomEntity(): List<BookedMeetingRoomEntity> {
    return documents.map { firebaseResponseFields ->
        firebaseResponseFields.fields.asEntity()
    }
}
