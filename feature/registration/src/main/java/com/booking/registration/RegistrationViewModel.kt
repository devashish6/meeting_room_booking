package com.booking.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.booking.data.repository.DataRepository
import com.booking.data.worker.initializeWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val workManager: WorkManager
) : ViewModel() {

    private val TAG = "REGISTRATION_VIEW_MODEL"

    private val _registrationUiState: MutableStateFlow<RegistrationUiState> =
        MutableStateFlow(RegistrationUiState.None)

    val registrationUiState: StateFlow<RegistrationUiState>
        get() = _registrationUiState.asStateFlow()

    fun registerUser(name: String, email: String, password: String, confirmedPassword: String) {
        initializeWorker(workManager)
        _registrationUiState.value = RegistrationUiState.Loading
        if (!email.contains("@") || !email.endsWith(".com")) {
            _registrationUiState.value = RegistrationUiState.InvalidEmailID
            Log.d(TAG, "registerUser: ${registrationUiState.value}")
        } else if (password != confirmedPassword) {
            _registrationUiState.value = RegistrationUiState.PasswordMismatch
            Log.d(TAG, "registerUser: ${registrationUiState.value}")
        } else {
            viewModelScope.launch {
                dataRepository.getAllUsers()
                val users = dataRepository.users
                val userExists = users.value.any { it?.email == email }
                if (userExists) {
                    _registrationUiState.value = RegistrationUiState.AccountAlreadyExists
                } else {
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
    }

}