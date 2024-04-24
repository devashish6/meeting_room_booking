package com.booking.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booking.data.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val TAG = "REGISTRATION_VIEW_MODEL"

    private val _registrationUiState : MutableStateFlow<RegistrationUiState> = MutableStateFlow(RegistrationUiState.None)

    val registrationUiState : StateFlow<RegistrationUiState>
        get() = _registrationUiState

    fun registerUser(name: String, email: String, password: String, confirmedPassword: String) {
        if (!email.contains("@") || !email.endsWith(".com")) {
            _registrationUiState.value = RegistrationUiState.InvalidEmailID
            Log.d(TAG, "registerUser: ${registrationUiState.value}")
            return
        }
        if (password != confirmedPassword) {
            _registrationUiState.value = RegistrationUiState.PasswordMismatch
            Log.d(TAG, "registerUser: ${registrationUiState.value}")
            return
        }
        viewModelScope.launch {
            dataRepository.getAllUsers()
            val users = dataRepository.users
            val userExists = users.value.any {it?.email == email}
            if (userExists) {
                _registrationUiState.value = RegistrationUiState.AccountAlreadyExists
                return@launch
            }
            val userCreation = dataRepository.createUser(name, email, password)
            if (!userCreation) {
                _registrationUiState.value = RegistrationUiState.None
            } else {
                _registrationUiState.value = RegistrationUiState.Success
            }
            Log.d(TAG, "registerUser: ${registrationUiState.value}")
        }
    }

}