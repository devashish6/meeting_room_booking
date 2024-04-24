package com.booking.database.com.booking.database.repository

import com.booking.database.model.BookedMeetingRoomEntity
import com.booking.database.model.MeetingRoomEntity
import com.booking.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInterface {
    suspend fun addUser(user: UserEntity) //not used
    suspend fun addUsers(users: List<UserEntity>)
    suspend fun getAllUsers() : List<UserEntity?>
    suspend fun getBookedMeetingRooms() : List<BookedMeetingRoomEntity?>
    suspend fun addBookedMeetingRooms(bookedMeetingRooms: List<BookedMeetingRoomEntity>)
    suspend fun getAllMeetingRooms() : List<MeetingRoomEntity?>
    suspend fun addMeetingRooms(meetingRooms: List<MeetingRoomEntity>)
    suspend fun getUserByEmail(userEmail: String) : UserEntity?
    suspend fun getMeetingRoomByID(meetingRoomID: String) : MeetingRoomEntity?
}