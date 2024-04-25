package com.booking.dashboard.ui.theme

import com.booking.model.model.BookedMeetingRoom
import kotlinx.coroutines.flow.MutableStateFlow

interface DashboardUiState {
    data class Success(val bookedMeetings: List<BookedMeetingRoom?>) : DashboardUiState
    data object Loading : DashboardUiState
    data object None : DashboardUiState


}