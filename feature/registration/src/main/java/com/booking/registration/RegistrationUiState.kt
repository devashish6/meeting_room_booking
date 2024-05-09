package com.booking.registration

interface RegistrationUiState {
    data object None : RegistrationUiState
    data object Loading : RegistrationUiState
    data object InvalidEmailID : RegistrationUiState
    data object AccountAlreadyExists : RegistrationUiState
    data object PasswordMismatch : RegistrationUiState
    data object Success : RegistrationUiState
}