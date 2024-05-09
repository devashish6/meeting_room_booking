package com.booking.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.booking.data.repository.DataRepository
import com.booking.data.worker.initializeWorker
import com.booking.model.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
                if (doesUserExist(users.value, email)) {
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

    private fun doesUserExist(users: List<User?>, email: String): Boolean {
        var result = false
        users.forEach {
            if (it?.email == email) {
                result = true
            }
        }
        return result
    }

}