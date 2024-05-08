package com.booking.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booking.data.repository.DataRepository
import com.booking.datastore.model.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val session: Session
) : ViewModel() {

//    private lateinit var syncWorker: SyncWorker

    private val TAG = "LOGIN_VIEWMODEL"

    private val _loginUiState : MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.None)

    val loginUiState : StateFlow<LoginUiState>
        get() = _loginUiState


    fun validateLoginCredentials(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                if (!email.contains("@") || !email.endsWith(".com")) {
                    _loginUiState.value = LoginUiState.InvalidEmailID
                    Log.d(TAG, "validateLoginCredentials: ${loginUiState.value}")
                    session.setUserLoggedIn(false)
                    return@launch
                }
                dataRepository.getUserByEmail(email)
                val user = dataRepository.user
                if (user.value?.email == email && user.value!!.password == password)  {
                    session.setUserLoggedIn(true)
                    session.setUserName(user.value!!.email)
                    _loginUiState.value = LoginUiState.Success(user.value!!)
                    Log.d(TAG, "validateLoginCredentials: ${loginUiState.value}")
                    return@launch
                }
            }
        }
        Log.d(TAG, "validateLoginCredentials: ${loginUiState.value}")
        _loginUiState.value = LoginUiState.InvalidCredentials
    }
}