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
    override suspend fun createUser(user: HashMap<String, String>): Response<FirebaseResponseFields<RemoteUser>> {
        return apiService.createUser(user)
    }

    override suspend fun getAllUsers(): Response<FirebaseDocument<RemoteUser>> {
        return apiService.getAllUsers()
    }

    override suspend fun getBookedMeetingRooms(): Response<FirebaseDocument<RemoteBookedMeetingRooms>> {
        return apiService.getBookedMeetingRooms()
    }

    override suspend fun getAllMeetingRooms(): Response<FirebaseDocument<RemoteMeetingRoom>> {
        return apiService.getAllMeetingRooms()
    }

    override suspend fun bookMeetingRoom(user: HashMap<String, Any>): Response<FirebaseResponseFields<RemoteBookedMeetingRooms>> {
        return apiService.bookMeetingRoom(user)
    }

    override suspend fun getUserByUserID(userID: String): Response<FirebaseResponseFields<RemoteUser>> {
        return apiService.getUserByUserID(userID)
    }

    override suspend fun getMeetingRoomByID(meetingRoomID: String): Response<FirebaseResponseFields<RemoteMeetingRoom>> {
        return apiService.getMeetingRoomByID(meetingRoomID)
    }

}