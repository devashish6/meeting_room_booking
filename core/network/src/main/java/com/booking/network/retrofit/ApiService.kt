package com.booking.network.retrofit

import com.booking.network.model.MeetingRoom
import com.booking.network.model.MeetingRooms
import com.booking.network.model.User
import com.booking.network.model.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    private companion object {
        const val FIREBASE_SLOTS = "v1/projects/slots-21fc8/databases/(default)/documents/"
    }
    @GET(FIREBASE_SLOTS + "users")
    suspend fun getAllUsers() : Users

    @GET(FIREBASE_SLOTS + "meeting_room")
    suspend fun getAllMeetingRooms() : MeetingRooms

    @GET(FIREBASE_SLOTS + "meeting_room/{meetingRoomID}")
    suspend fun getMeetingRoomByID(@Path("meetingRoomID") meetingRoomID: String, ): Response<MeetingRoom>
    @GET(FIREBASE_SLOTS + "users/{userID}")
    suspend fun getUserByUserID(@Path("userID") userID: String, ): Response<User>

    @POST(FIREBASE_SLOTS + "users")
    suspend fun createUser(@Body user: User) : Response<User>

}