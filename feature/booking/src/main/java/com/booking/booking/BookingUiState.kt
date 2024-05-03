package com.booking.booking


interface BookingUiState {

    data object Success: BookingUiState

    data object Loading : BookingUiState

    data object NoMeetingRoomsAvailable : BookingUiState

    data object None : BookingUiState
    data object InvalidDetails : BookingUiState

}