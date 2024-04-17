package com.booking.data.repository

import com.booking.data.model.asEntity
import com.booking.data.model.asListOfBookedMeetingRoomEntity
import com.booking.data.model.asListOfMeetingRoomEntity
import com.booking.data.model.asListOfUserEntity
import com.booking.database.com.booking.database.repository.LocalDataSourceInterface
import com.booking.database.model.BookedMeetingRoomEntity
import com.booking.database.model.MeetingRoomEntity
import com.booking.database.model.UserEntity
import com.booking.network.retrofit.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val localDataSourceInterface: LocalDataSourceInterface,
    private val remoteDataSource: RemoteDataSource
) {
    private val _users = MutableStateFlow<List<UserEntity>>(emptyList())
    private val _meetingRooms = MutableStateFlow<List<MeetingRoomEntity>>(emptyList())
    private val _bookedMeetingRooms = MutableStateFlow<List<BookedMeetingRoomEntity>>(emptyList())

    val users : StateFlow<List<UserEntity>>
        get() = _users

    val meetingRooms : StateFlow<List<MeetingRoomEntity>>
        get() = _meetingRooms

    val bookedMeetingRooms : StateFlow<List<BookedMeetingRoomEntity>>
        get() = _bookedMeetingRooms

    suspend fun getAllUsers() {
        val offlineResponse = localDataSourceInterface.getAllUsers()
        if (offlineResponse.isNotEmpty()) {
            _users.emit(offlineResponse)
        } else {
            val response = remoteDataSource.getAllUsers()
            if (response.isSuccessful && response.body() != null) {
                localDataSourceInterface.addUsers(response.body()!!.asListOfUserEntity())
                _users.emit(response.body()!!.asListOfUserEntity())
            }
        }
    }

    suspend fun getAllMeetingRooms() {
        val offlineResponse = localDataSourceInterface.getAllMeetingRooms()
        if (offlineResponse.isNotEmpty()) {
            _meetingRooms.emit(offlineResponse)
        } else {
            val response = remoteDataSource.getAllMeetingRooms()
            if (response.isSuccessful && response.body() != null) {
                localDataSourceInterface.addMeetingRooms(response.body()!!.asListOfMeetingRoomEntity())
                _meetingRooms.emit(response.body()!!.asListOfMeetingRoomEntity())
            }
        }
    }

    suspend fun getBookedMeetingRooms() {
        val offlineResponse = localDataSourceInterface.getBookedMeetingRooms()
        if (offlineResponse.isNotEmpty()) {
            _bookedMeetingRooms.emit(offlineResponse)
        } else {
            val response = remoteDataSource.getBookedMeetingRooms()
            if (response.isSuccessful && response.body() != null) {
                localDataSourceInterface.addBookedMeetingRooms(response.body()!!.asListOfBookedMeetingRoomEntity())
                _bookedMeetingRooms.emit(response.body()!!.asListOfBookedMeetingRoomEntity())
            }
        }
    }

//    suspend fun getUserByUserID(userID: String) {
//        val offlineResponse = listOf(localDataSourceInterface.getUserByUserID(userID))
//        if (offlineResponse.isNotEmpty()) {
//            _users.emit(offlineResponse)
//        } else {
//            val response = remoteDataSource.getAllUsers()
//            if (response.isSuccessful && response.body() != null) {
//                localDataSourceInterface.addUsers(response.body()!!.asListOfUserEntity())
//                _users.emit(response.body()!!.asListOfUserEntity())
//            }
//        }
//    }
}