package com.booking.dashboard.ui.theme

import com.booking.model.model.BookedMeetingRoom

data class BookingsUiState(
    var selectedDate: String,
    var bookedSlots: List<BookedMeetingRoom>
)