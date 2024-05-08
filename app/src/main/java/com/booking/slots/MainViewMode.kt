package com.booking.slots

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booking.datastore.model.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val session: Session
) : ViewModel(){

    val isLoggedIn = session.isUserLoggedIn()
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = false)
    val userName = session.getUserName()
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = "")
}