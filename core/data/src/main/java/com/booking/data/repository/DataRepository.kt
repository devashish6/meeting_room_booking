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
import com.booking.network.utils.convertToFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val localDataSourceInterface: LocalDataSourceInterface,
    private val remoteDataSource: RemoteDataSource
) {
    private val TAG = "DataRepository_DEBUG"

    private val _offlineUsers = MutableStateFlow<List<UserEntity?>>(emptyList())
    private val _offlineUser = MutableStateFlow<UserEntity?>(null)
    private val _offlineMeetingRooms = MutableStateFlow<List<MeetingRoomEntity?>>(emptyList())
    private val _offlineBookedMeetingRooms =
        MutableStateFlow<List<BookedMeetingRoomEntity?>>(emptyList())

    private val _users = MutableStateFlow<List<User?>>(emptyList())
    private val _user = MutableStateFlow<User?>(null)
    private val _meetingRooms = MutableStateFlow<List<MeetingRoom?>>(emptyList())
    private val _bookedMeetingRooms = MutableStateFlow<List<BookedMeetingRoom?>>(emptyList())

    val offlineUsers: StateFlow<List<UserEntity?>>
        get() = _offlineUsers

    val offlineMeetingRooms: StateFlow<List<MeetingRoomEntity?>>
        get() = _offlineMeetingRooms

    val offlineBookedMeetings: StateFlow<List<BookedMeetingRoomEntity?>>
        get() = _offlineBookedMeetingRooms

    val users: StateFlow<List<User?>>
        get() = _users

    val user: StateFlow<User?>
        get() = _user

    val meetingRooms: StateFlow<List<MeetingRoom?>>
        get() = _meetingRooms

    val bookedMeetingRooms: StateFlow<List<BookedMeetingRoom?>>
        get() = _bookedMeetingRooms

    suspend fun getAllUsers() {
        val offlineResponse = localDataSourceInterface.getAllUsers()
        if (offlineResponse.isNotEmpty()) {
            _offlineUsers.emit(offlineResponse)
            _users.emit(offlineResponse.map { it?.asDomainModel() })
            Log.d(TAG, "getAllUsers: $_users")
        } else {
            _offlineUsers.emit(emptyList())
            _users.emit(emptyList())
        }
    }

    suspend fun getAllMeetingRooms() {
        val offlineResponse = localDataSourceInterface.getAllMeetingRooms()
        if (offlineResponse.isNotEmpty()) {
            _offlineMeetingRooms.emit(offlineResponse)
            _meetingRooms.emit(offlineResponse.map { it?.asDomainModel() })
            Log.d(TAG, "getAllMeetingRooms: $_meetingRooms")
        } else {
            _offlineMeetingRooms.emit(emptyList())
            _meetingRooms.emit(emptyList())
        }
    }

    suspend fun getBookedMeetingRooms() {
        val offlineResponse = localDataSourceInterface.getBookedMeetingRooms()
        if (offlineResponse.isNotEmpty()) {
            _offlineBookedMeetingRooms.emit(offlineResponse)
            _bookedMeetingRooms.emit(offlineResponse.map { it?.asDomainModel() })
            Log.d(TAG, "getBookedMeetingRooms: $_bookedMeetingRooms")
        } else {
            _offlineBookedMeetingRooms.emit(emptyList())
            _bookedMeetingRooms.emit(emptyList())
        }
    }

    suspend fun syncWith(): Boolean {
        return try {
            Log.d(TAG, "syncWith: Calling remote")
            val remoteUsers = remoteDataSource.getAllUsers()
            Log.d(TAG, "syncWith: remote users : $remoteUsers")
            val remoteMeetingRooms = remoteDataSource.getAllMeetingRooms()
            Log.d(TAG, "syncWith: remote meeting rooms : $remoteMeetingRooms")
            val remoteBookedMeetingRooms = remoteDataSource.getBookedMeetingRooms()
            Log.d(TAG, "syncWith: remote booked meeting rooms : $remoteBookedMeetingRooms")
            localDataSourceInterface.addUsers(remoteUsers.asListOfUserEntity())
            Log.d(TAG, "syncWith: adding users to database : ${remoteUsers.asListOfUserEntity()}")
            _users.emit(remoteUsers.asListOfUserEntity().map { it.asDomainModel() })
            localDataSourceInterface.addMeetingRooms(remoteMeetingRooms.asListOfMeetingRoomEntity())
            Log.d(
                TAG,
                "syncWith: adding meeting room data to database : ${remoteMeetingRooms.asListOfMeetingRoomEntity()}"
            )
            _meetingRooms.emit(
                remoteMeetingRooms.asListOfMeetingRoomEntity().map { it.asDomainModel() })
            localDataSourceInterface.addBookedMeetingRooms(remoteBookedMeetingRooms.asListOfBookedMeetingRoomEntity())
            Log.d(
                TAG,
                "syncWith: adding booked meetings : ${remoteBookedMeetingRooms.asListOfBookedMeetingRoomEntity()}"
            )
            _bookedMeetingRooms.emit(
                remoteBookedMeetingRooms.asListOfBookedMeetingRoomEntity()
                    .map { it.asDomainModel() })
            true
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage?.toString() ?: "")
            false
        }
    }

    suspend fun getUserByEmail(emailID: String) {
        val userDetails = localDataSourceInterface.getUserByEmail(emailID)
        Log.d(TAG, "getUserByEmail: $userDetails")
        if (userDetails != null) {
            _offlineUser.emit(userDetails)
            _user.emit(userDetails.asDomainModel())
        } else {
            _offlineUser.emit(null)
            _user.emit(null)
        }
    }

    suspend fun createUser(name: String, emailID: String, password: String): Boolean {
        val userHashMap = HashMap<String, String>()
        userHashMap["user_email"] = emailID
        userHashMap["user_name"] = name
        userHashMap["user_password"] = password
        Log.d(TAG, "createUser request body: " + convertToCustomFormat(userHashMap).toString())
        return try {
            val user = remoteDataSource.createUser(convertToCustomFormat(userHashMap))
            Log.d(TAG, "createUser: $user")
            true
        } catch (e: Exception) {
            Log.e(TAG, "createUser: ${e.localizedMessage}")
            false
        }
    }

    suspend fun bookMeetingRoom(
        startTime: String,
        endTime: String,
        title: String,
        meetingRoomId: String,
        host: String,
        date: String,
        attendees: List<String>
    ) : Boolean {

        val bookingHashMap = HashMap<String, Any>()
        bookingHashMap["from_time"] = startTime
        bookingHashMap["to_time"] = endTime
        bookingHashMap["meeting_room_id"] = meetingRoomId
        bookingHashMap["host"] = host
        bookingHashMap["date"] = date
        bookingHashMap["meeting_title"] = title
        bookingHashMap["attendees"] = attendees

        return try {
            val requestJson = convertToFormat(bookingHashMap)
            Log.d(TAG, "bookMeetingRoom: $requestJson")
            remoteDataSource.bookMeetingRoom(requestJson)
            true
        } catch (e: Exception) {
            Log.e(TAG, "bookMeetingRoom: ${e.stackTrace}")
            false
        }
    }

    suspend fun markTimeSlots(date: String) {
        val bookings = localDataSourceInterface.getBookingsForDate(date)
        if (bookings.isNotEmpty()) {
            _offlineBookedMeetingRooms.emit(bookings)
            _bookedMeetingRooms.emit(bookings.map { it?.asDomainModel() })
            Log.d(TAG, "markTimeSlots: ${_bookedMeetingRooms.value}")
        } else {
            _bookedMeetingRooms.emit(emptyList())
        }
    }

    suspend fun getAvailableMeetingRooms(date: String, fromTime: String, toTime: String) {
        val availableRooms = localDataSourceInterface.getAvailableMeetingRooms(date, fromTime, toTime)
        if (availableRooms.isNotEmpty()) {
            _offlineMeetingRooms.emit(availableRooms)
            _meetingRooms.emit(availableRooms.map { it?.asDomainModel() })
            Log.d(TAG, "getAvailableMeetingRooms: ${_meetingRooms.value}")
        } else {
            Log.d(TAG, "getAvailableMeetingRooms: empty")
            _meetingRooms.emit(emptyList())
        }
    }

    suspend fun searchForUsers(email: String) {
        val result = localDataSourceInterface.searchUsers(email)
        if (result.isNotEmpty()) {
            _offlineUsers.emit(result)
            _users.emit(result.map { it?.asDomainModel() })
        } else {
            _users.emit(emptyList())
        }
    }
}