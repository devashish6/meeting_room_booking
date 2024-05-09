package com.booking.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.booking.data.repository.DataRepository
import com.booking.data.worker.initializeWorker
import com.booking.datastore.model.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val session: Session,
    private val workManager: WorkManager
) : ViewModel() {

    private val TAG = "LOGIN_VIEWMODEL"

    private val _loginUiState : MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.None)

    val loginUiState : StateFlow<LoginUiState>
        get() = _loginUiState

    fun setLoginUiState(state: LoginUiState) {
        _loginUiState.update { state }
    }


    fun validateLoginCredentials(email: String, password: String) {
        initializeWorker(workManager)
        setLoginUiState(LoginUiState.Loading)
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                if (!email.contains("@") || !email.endsWith(".com")) {
                    setLoginUiState(LoginUiState.InvalidEmailID)
                    Log.d(TAG, "validateLoginCredentials: ${loginUiState.value}")
                    session.setUserLoggedIn(false)
                    return@launch
                }
                dataRepository.getUserByEmail(email)
                val user = dataRepository.user
                if (user.value?.email == email && user.value!!.password == password)  {
                    session.setUserLoggedIn(true)
                    session.setUserName(user.value!!.email)
                    setLoginUiState(LoginUiState.Success(user.value!!))
                    Log.d(TAG, "validateLoginCredentials: ${loginUiState.value}")
                } else {
                    Log.d(TAG, "validateLoginCredentials: ${loginUiState.value}")
                    setLoginUiState(LoginUiState.InvalidCredentials)
                }
            }
        }
    }
}