package com.booking.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.booking.data.repository.DataRepository
import com.booking.model.model.BookedMeetingRoom
import com.booking.model.model.MeetingRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val workManager: WorkManager
) : ViewModel() {

    private val TAG = "BOOKING_VIEWMODEL"

    private val _bookingsUiState = MutableStateFlow<BookingUiState>(BookingUiState.None)

    val bookingUiState: StateFlow<BookingUiState>
        get() = _bookingsUiState

    val meetingRooms: StateFlow<List<MeetingRoom?>>
        get() = dataRepository.meetingRooms

    val bookedMeetingRooms: StateFlow<List<BookedMeetingRoom?>>
        get() = dataRepository.bookedMeetingRooms

    fun bookMeetingRoom(
        startTime: String,
        endTime: String,
        title: String,
        meetingRoomId: String,
        host: String,
        date: String,
        attendees: List<String>
    ) {
        _bookingsUiState.value = BookingUiState.Loading
        if (basicValidation(startTime, endTime, date)|| title.isEmpty() || meetingRoomId.isEmpty() || host.isEmpty() || attendees.isEmpty()) {
            _bookingsUiState.value = BookingUiState.InvalidDetails
            return
        }
        try {
            val validStartTime = startTime.toInt()
            val validEndTime = endTime.toInt()
            if (validEndTime < validStartTime) {
                _bookingsUiState.value = BookingUiState.InvalidDetails
                return
            }
        } catch (e: Exception) {
            _bookingsUiState.value = BookingUiState.InvalidDetails
            return
        }
        viewModelScope.launch {
            val bookingStatus = dataRepository.bookMeetingRoom(
                startTime = startTime,
                endTime = endTime,
                title = title,
                meetingRoomId = meetingRoomId,
                host = host,
                date = date,
                attendees = attendees
            )
            if (bookingStatus) {
                _bookingsUiState.value = BookingUiState.Success
            } else {
                _bookingsUiState.value = BookingUiState.None
            }
        }
    }

    private fun basicValidation(startTime: String, endTime: String, date: String): Boolean {
        return startTime.isEmpty() || endTime.isEmpty() || date.isEmpty()
    }

    fun getAvailableMeetingRooms(startTime: String, endTime: String, date: String) {
        if (basicValidation(startTime, endTime, date)) {
            _bookingsUiState.value = BookingUiState.InvalidDetails
            return
        }
        viewModelScope.launch {
            dataRepository.getAvailableMeetingRooms(date = date, fromTime = startTime, toTime = endTime)
        }

    }

}