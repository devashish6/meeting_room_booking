package com.booking.model.model

data class BookedMeetingRoom(
    var meetingRoomBookID: String,
    var meetingRoomID: String,
    var fromTime: String,
    var toTime: String,
    var host: String,
    var date: String,
    var meetingTitle: String,
    var attendees: List<String>
)
