package com.booking.booking


interface BookingUiState {

    data object BookingSuccess: BookingUiState

    data object Loading : BookingUiState

    data object NoMeetingRoomsAvailable : BookingUiState

    data object None : BookingUiState
    data object NoAvailableUsers : BookingUiState
    data object InvalidDetails : BookingUiState

}