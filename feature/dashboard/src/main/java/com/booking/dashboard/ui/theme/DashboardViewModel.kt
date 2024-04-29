package com.booking.dashboard.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booking.data.repository.DataRepository
import com.booking.model.model.BookedMeetingRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dataRepository : DataRepository
) : ViewModel() {
    private  val TAG = "DASHBOARD_VIEWMODEL"
    private val _dashboardUiState : MutableStateFlow<DashboardUiState> = MutableStateFlow(DashboardUiState.None)
    val dashboardUiState : StateFlow<DashboardUiState>
        get() = _dashboardUiState

    fun getBookedTimeslots(date: String) {
        Log.d(TAG, "getBookedTimeslots: $date")
        _dashboardUiState.value = DashboardUiState.Loading
        viewModelScope.launch {
            dataRepository.markTimeSlots(date = date)
            val bookedMeetings = dataRepository.bookedMeetingRooms
            if (bookedMeetings.value.isNotEmpty()) {
                Log.d(TAG, "getBookedTimeslots: ${bookedMeetings.value}")
                _dashboardUiState.value = DashboardUiState.Success(bookedMeetings.value.filterNotNull())
            } else {
                Log.d(TAG, "getBookedTimeslots: empty database ? ${dataRepository.bookedMeetingRooms}")
                _dashboardUiState.value = DashboardUiState.None
            }
        }
    }
}