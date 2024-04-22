package com.booking.network.retrofit

import com.booking.network.model.RemoteBookedMeetingRooms
import com.booking.network.model.FirebaseDocument
import com.booking.network.model.FirebaseResponseFields
import com.booking.network.model.RemoteMeetingRoom
import com.booking.network.model.RemoteUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    private companion object {
        const val FIREBASE_SLOTS = "v1/projects/slots-21fc8/databases/(default)/documents/"
        const val MEETING_ROOM = "meeting_room"
        const val USERS = "users"
        const val MEETING_ROOM_BOOKING = "meeting_room_booking"
        const val USER_ID = "userID"
        const val MEETING_ROOM_ID = "meetingRoomID"
    }

    @POST(FIREBASE_SLOTS + USERS)
    suspend fun createUser(@Body user: HashMap<String, String>) : FirebaseResponseFields<RemoteUser> //call on click of register - Register feature

//    @PATCH("$FIREBASE_SLOTS$USERS/{$USER_ID}")//Use emailID as primary key
//    suspend fun updateUserID(@Path(USER_ID) userID: String, @Body user: HashMap<String, String>) : Response<FirebaseResponseFields<RemoteUser>> //call on success of register - Register feature

    @GET(FIREBASE_SLOTS + USERS)
    suspend fun getAllUsers() : FirebaseDocument<RemoteUser> //call while logging in (check for email and password information) - Login feature

    @GET(FIREBASE_SLOTS + MEETING_ROOM_BOOKING)
    suspend fun getBookedMeetingRooms() : FirebaseDocument<RemoteBookedMeetingRooms> //call in dashboard - dashboard feature

    @GET(FIREBASE_SLOTS + MEETING_ROOM)
    suspend fun getAllMeetingRooms() : FirebaseDocument<RemoteMeetingRoom> //call in booking screen before booking - booking feature

    @POST(FIREBASE_SLOTS + MEETING_ROOM_BOOKING)
    suspend fun bookMeetingRoom(@Body user: HashMap<String, Any>) : FirebaseResponseFields<RemoteBookedMeetingRooms> //call in booking screen - booking feature

    @GET("$FIREBASE_SLOTS$USERS/{$USER_ID}")
    suspend fun getUserByUserID(@Path(USER_ID) userID: String, ): FirebaseResponseFields<RemoteUser> // call in agenda screen - agenda feature

    @GET("$FIREBASE_SLOTS$MEETING_ROOM/{$MEETING_ROOM_ID}")
    suspend fun getMeetingRoomByID(@Path(MEETING_ROOM_ID) meetingRoomID: String, ): FirebaseResponseFields<RemoteMeetingRoom>
}

