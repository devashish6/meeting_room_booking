package com.booking.dashboard.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.booking.data.repository.DataRepository
import com.booking.data.worker.initializeWorker
import com.booking.datastore.model.Session
import com.booking.model.model.BookedMeetingRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val workManager: WorkManager,
    private val session: Session
) : ViewModel() {

    private val TAG = "DASHBOARD_VIEWMODEL"

    val workerState = MutableStateFlow(WorkInfo.State.RUNNING)

    private val _dashboardUiState: MutableStateFlow<DashboardUiState> =
        MutableStateFlow(DashboardUiState.Loading)
    val dashboardUiState: StateFlow<DashboardUiState>
        get() = _dashboardUiState

    val bookedMeetingRooms: StateFlow<List<BookedMeetingRoom?>>
        get() = dataRepository.bookedMeetingRooms

    fun getBookedTimeslots(date: String) {
        initializeWorker(workManager)
        _dashboardUiState.value = DashboardUiState.Loading
        viewModelScope.launch {
            dataRepository.markTimeSlots(date = date)
            val bookedMeetings = dataRepository.bookedMeetingRooms
            if (bookedMeetings.value.isNotEmpty()) {
                Log.d(TAG, "getBookedTimeslots: ${bookedMeetings.value}")
                _dashboardUiState.value =
                    DashboardUiState.Success(bookedMeetings.value.filterNotNull())
            } else {
                Log.d(
                    TAG,
                    "getBookedTimeslots: empty database ? ${dataRepository.bookedMeetingRooms.value}"
                )
                _dashboardUiState.value = DashboardUiState.None
            }
            val id = workManager.getWorkInfosForUniqueWork("SyncWorkName").get()[0].id
            workManager.getWorkInfoByIdFlow(id).collect {
                workerState.value = it.state
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            session.setUserLoggedIn(false)
            session.setUserName("")
        }
    }

}