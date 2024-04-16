package com.booking.model

data class BookedMeetingRoom(
    var meetingRoomBookingID: String,
    var meetingRoomID: String,
    var fromTime: String,
    var toTime: String,
    var attendees: List<String>,
    var host: String,
    var date: String,
    var meetingTitle: String
)
