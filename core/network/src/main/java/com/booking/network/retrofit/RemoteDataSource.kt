package com.booking.network.retrofit

import com.booking.network.model.FirebaseDocument
import com.booking.network.model.FirebaseResponseFields
import com.booking.network.model.RemoteBookedMeetingRooms
import com.booking.network.model.RemoteMeetingRoom
import com.booking.network.model.RemoteUser
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) : ApiService {
    override suspend fun createUser(user: HashMap<String, String>): FirebaseResponseFields<RemoteUser> {
        return apiService.createUser(user)
    }

    override suspend fun getAllUsers(): FirebaseDocument<RemoteUser> {
        return apiService.getAllUsers()
    }

    override suspend fun getBookedMeetingRooms(): FirebaseDocument<RemoteBookedMeetingRooms> {
        return apiService.getBookedMeetingRooms()
    }

    override suspend fun getAllMeetingRooms(): FirebaseDocument<RemoteMeetingRoom> {
        return apiService.getAllMeetingRooms()
    }

    override suspend fun bookMeetingRoom(user: HashMap<String, Any>): FirebaseResponseFields<RemoteBookedMeetingRooms> {
        return apiService.bookMeetingRoom(user)
    }

    override suspend fun getUserByUserID(userID: String): FirebaseResponseFields<RemoteUser> {
        return apiService.getUserByUserID(userID)
    }

    override suspend fun getMeetingRoomByID(meetingRoomID: String): FirebaseResponseFields<RemoteMeetingRoom> {
        return apiService.getMeetingRoomByID(meetingRoomID)
    }

}