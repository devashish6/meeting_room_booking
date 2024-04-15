package com.booking.network.retrofit

import com.booking.network.model.FirebaseDocument
import com.booking.network.model.FirebaseResponseFields
import com.booking.network.model.MeetingRoom
import com.booking.network.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    private companion object {
        const val FIREBASE_SLOTS = "v1/projects/slots-21fc8/databases/(default)/documents/"
    }
    @GET(FIREBASE_SLOTS + "meeting_room")
    suspend fun getAllMeetingRooms() : FirebaseDocument<MeetingRoom>

    @GET(FIREBASE_SLOTS + "meeting_room/{meetingRoomID}")
    suspend fun getMeetingRoomByID(@Path("meetingRoomID") meetingRoomID: String, ): Response<FirebaseResponseFields<MeetingRoom>>

    @GET(FIREBASE_SLOTS + "users")
    suspend fun getAllUsers() : FirebaseDocument<User>

    @GET(FIREBASE_SLOTS + "users/{userID}")
    suspend fun getUserByUserID(@Path("userID") userID: String, ): Response<FirebaseResponseFields<User>>

    @POST(FIREBASE_SLOTS + "users")
    suspend fun createUser(@Body user: HashMap<String, String>) : Response<FirebaseResponseFields<User>>

    @POST(FIREBASE_SLOTS + "meeting_room_booking")
    suspend fun bookMeetingRoom(@Body user: HashMap<String, Any>) : Response<FirebaseResponseFields<User>>

    @GET(FIREBASE_SLOTS + "meeting_room_booking")
    suspend fun getBookedMeetingRooms() : Response<FirebaseDocument<User>>

}

