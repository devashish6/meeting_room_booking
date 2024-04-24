package com.booking.data.repository

import android.util.Log
import com.booking.data.model.asDomainModel
import com.booking.data.model.asListOfBookedMeetingRoomEntity
import com.booking.data.model.asListOfMeetingRoomEntity
import com.booking.data.model.asListOfUserEntity
import com.booking.database.com.booking.database.repository.LocalDataSourceInterface
import com.booking.database.model.BookedMeetingRoomEntity
import com.booking.database.model.MeetingRoomEntity
import com.booking.database.model.UserEntity
import com.booking.model.model.BookedMeetingRoom
import com.booking.model.model.MeetingRoom
import com.booking.model.model.User
import com.booking.network.retrofit.RemoteDataSource
import com.booking.network.utils.convertToCustomFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val localDataSourceInterface: LocalDataSourceInterface,
    private val remoteDataSource: RemoteDataSource
)  {
    private val TAG = "DataRepository_DEBUG"

    private val _offlineUsers = MutableStateFlow<List<UserEntity?>>(emptyList())
    private val _offlineUser = MutableStateFlow<UserEntity?>(null)
    private val _offlineMeetingRooms = MutableStateFlow<List<MeetingRoomEntity?>>(emptyList())
    private val _offlineBookedMeetingRooms = MutableStateFlow<List<BookedMeetingRoomEntity?>>(emptyList())

    private val _users = MutableStateFlow<List<User?>>(emptyList())
    private val _user = MutableStateFlow<User?>(null)
    private val _meetingRooms = MutableStateFlow<List<MeetingRoom?>>(emptyList())
    private val _bookedMeetingRooms = MutableStateFlow<List<BookedMeetingRoom?>>(emptyList())

    val offlineUsers : StateFlow<List<UserEntity?>>
        get() = _offlineUsers

    val offlineMeetingRooms : StateFlow<List<MeetingRoomEntity?>>
        get() = _offlineMeetingRooms

    val offlineBookedMeetings : StateFlow<List<BookedMeetingRoomEntity?>>
        get() = _offlineBookedMeetingRooms

    val users : StateFlow<List<User?>>
        get() = _users

    val user : StateFlow<User?>
        get() = _user

    val meetingRooms : StateFlow<List<MeetingRoom?>>
        get() = _meetingRooms

    val bookedMeetingRooms : StateFlow<List<BookedMeetingRoom?>>
        get() = _bookedMeetingRooms

    suspend fun getAllUsers() {
        val offlineResponse = localDataSourceInterface.getAllUsers()
        if (offlineResponse.isNotEmpty()) {
            _offlineUsers.emit(offlineResponse)
            _users.emit(offlineResponse.map { it?.asDomainModel() })
            Log.d(TAG, "getAllUsers: $_users")
        }
    }

    suspend fun getAllMeetingRooms() {
        val offlineResponse = localDataSourceInterface.getAllMeetingRooms()
        if (offlineResponse.isNotEmpty()) {
            _offlineMeetingRooms.emit(offlineResponse)
            _meetingRooms.emit(offlineResponse.map { it?.asDomainModel() })
            Log.d(TAG, "getAllMeetingRooms: $_meetingRooms")
        }
    }

    suspend fun getBookedMeetingRooms() {
        val offlineResponse = localDataSourceInterface.getBookedMeetingRooms()
        if (offlineResponse.isNotEmpty()) {
            _offlineBookedMeetingRooms.emit(offlineResponse)
            _bookedMeetingRooms.emit(offlineResponse.map { it?.asDomainModel() })
            Log.d(TAG, "getBookedMeetingRooms: $_bookedMeetingRooms")
        }
    }

    suspend fun syncWith(): Boolean {
        return try {
            val remoteUsers = remoteDataSource.getAllUsers()
            val remoteMeetingRooms = remoteDataSource.getAllMeetingRooms()
            val remoteBookedMeetingRooms = remoteDataSource.getBookedMeetingRooms()
            localDataSourceInterface.addUsers(remoteUsers.asListOfUserEntity())
            _users.emit(remoteUsers.asListOfUserEntity().map { it.asDomainModel() })
            localDataSourceInterface.addMeetingRooms(remoteMeetingRooms.asListOfMeetingRoomEntity())
            _meetingRooms.emit(remoteMeetingRooms.asListOfMeetingRoomEntity().map { it.asDomainModel() })
            localDataSourceInterface.addBookedMeetingRooms(remoteBookedMeetingRooms.asListOfBookedMeetingRoomEntity())
            _bookedMeetingRooms.emit(remoteBookedMeetingRooms.asListOfBookedMeetingRoomEntity().map { it.asDomainModel() })
            true
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage?.toString() ?: "")
            false
        }
    }

    suspend fun getUserByEmail(emailID: String) {
        val userDetails = localDataSourceInterface.getUserByEmail(emailID)
        Log.d(TAG, "getUserByEmail: $userDetails")
        _offlineUser.emit(userDetails)
        _user.emit(userDetails?.asDomainModel())
    }

    suspend fun createUser (name: String, emailID: String, password: String) : Boolean {
        val userHashMap = HashMap<String, String>()
        userHashMap["user_email"] = emailID
        userHashMap["user_name"] = name
        userHashMap["user_password"] = password
        Log.d(TAG, "createUser request body: "+ convertToCustomFormat(userHashMap).toString())
        return try {
            val user = remoteDataSource.createUser(convertToCustomFormat(userHashMap))
            Log.d(TAG, "createUser: $user")
            true
        } catch (e: Exception) {
            Log.e(TAG, "createUser: ${e.localizedMessage}", )
            false
        }
    }
}