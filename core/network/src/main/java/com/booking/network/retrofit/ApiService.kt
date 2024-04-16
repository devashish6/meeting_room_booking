package com.booking.network.retrofit

import com.booking.network.model.BookedMeetingRoom
import com.booking.network.model.FirebaseDocument
import com.booking.network.model.FirebaseResponseFields
import com.booking.network.model.RemoteMeetingRoom
import com.booking.network.model.RemoteUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    private companion object {
        const val FIREBASE_SLOTS = "v1/projects/slots-21fc8/databases/(default)/documents/"
    }
    @GET(FIREBASE_SLOTS + "meeting_room")
    suspend fun getAllMeetingRooms() : FirebaseDocument<RemoteMeetingRoom>

    @GET(FIREBASE_SLOTS + "meeting_room/{meetingRoomID}")
    suspend fun getMeetingRoomByID(@Path("meetingRoomID") meetingRoomID: String, ): Response<FirebaseResponseFields<RemoteMeetingRoom>>

    @GET(FIREBASE_SLOTS + "users")
    suspend fun getAllUsers() : FirebaseDocument<RemoteUser>

    @GET(FIREBASE_SLOTS + "users/{userID}")
    suspend fun getUserByUserID(@Path("userID") userID: String, ): Response<FirebaseResponseFields<RemoteUser>>

    @POST(FIREBASE_SLOTS + "users")
    suspend fun createUser(@Body user: HashMap<String, String>) : Response<FirebaseResponseFields<RemoteUser>>

    @POST(FIREBASE_SLOTS + "meeting_room_booking")
    suspend fun bookMeetingRoom(@Body user: HashMap<String, Any>) : Response<FirebaseResponseFields<BookedMeetingRoom>>

    @GET(FIREBASE_SLOTS + "meeting_room_booking")
    suspend fun getBookedMeetingRooms() : Response<FirebaseDocument<BookedMeetingRoom>>

    @PATCH(FIREBASE_SLOTS + "users/{userID}")
    suspend fun updateUserID(@Path("userID") userID: String, @Body user: HashMap<String, String>) : Response<FirebaseResponseFields<RemoteUser>>
}

