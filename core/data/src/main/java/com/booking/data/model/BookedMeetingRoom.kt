package com.booking.data.model

import com.booking.database.model.BookedMeetingRoomEntity
import com.booking.database.model.MeetingRoomEntity
import com.booking.network.model.FirebaseDocument
import com.booking.network.model.RemoteBookedMeetingRooms
import com.booking.network.model.RemoteMeetingRoom

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


fun FirebaseDocument<RemoteBookedMeetingRooms>.asListOfBookedMeetingRoomEntity(): List<BookedMeetingRoomEntity> {
    return documents.map { firebaseResponseFields ->
        firebaseResponseFields.fields.asEntity()
    }
}
