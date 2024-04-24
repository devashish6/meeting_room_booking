package com.booking.login

import com.booking.model.model.User

sealed interface LoginUiState {
    data class Success(val user: User) : LoginUiState
    data object InvalidCredentials : LoginUiState
    data object InvalidEmailID : LoginUiState
    data object None : LoginUiState
}