package com.booking.dashboard.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import com.booking.model.model.BookedMeetingRoom

internal class BookingsModifier(
    private val bookedMeetingRoom: BookedMeetingRoom
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = bookedMeetingRoom
}

internal fun Modifier.eventData(bookedMeetingRoom: BookedMeetingRoom) = this.then(BookingsModifier(bookedMeetingRoom))