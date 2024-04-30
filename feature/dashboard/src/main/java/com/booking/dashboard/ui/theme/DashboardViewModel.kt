package com.booking.dashboard.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booking.data.repository.DataRepository
import com.booking.model.model.BookedMeetingRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dataRepository: DataRepository,
) : ViewModel() {

//    var bookingUiState: StateFlow<BookingsUiState>? = null

    private val TAG = "DASHBOARD_VIEWMODEL"
    private val _dashboardUiState: MutableStateFlow<DashboardUiState> =
        MutableStateFlow(DashboardUiState.None)

    val dashboardUiState: StateFlow<DashboardUiState>
        get() = _dashboardUiState

    var selectedDate: MutableStateFlow<String> = MutableStateFlow("")

    val bookedMeetings: StateFlow<List<BookedMeetingRoom?>>
        get() = dataRepository.bookedMeetingRooms

    var bookingUiState = selectedDate.map {
        Log.d("inside ui state",selectedDate.value.toString())
        BookingsUiState(
            selectedDate = it,
            bookedSlots = getBookedTimeslots().filterNotNull()
        )
    }

    fun getBookedTimeslots(): List<BookedMeetingRoom?> {
        Log.d(TAG, "getBookedTimeslots: $selectedDate")
        _dashboardUiState.value = DashboardUiState.Loading
        viewModelScope.launch {
            selectedDate.value.let { dataRepository.markTimeSlots(date = it) }
            if (bookedMeetings.value.isNotEmpty()) {
                Log.d(TAG, "getBookedTimeslots: ${bookedMeetings.value}")
                _dashboardUiState.value =
                    DashboardUiState.Success(
                        bookedMeetings.value.filterNotNull()
                    )
            } else {
                Log.d(
                    TAG,
                    "getBookedTimeslots: empty database ? ${dataRepository.bookedMeetingRooms}"
                )
                _dashboardUiState.value = DashboardUiState.None
            }
        }
        return dataRepository.bookedMeetingRooms.value.filterNotNull()
    }
}