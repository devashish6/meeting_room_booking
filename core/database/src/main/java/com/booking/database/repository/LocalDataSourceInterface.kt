package com.booking.database.com.booking.database.repository

import com.booking.database.model.BookedMeetingRoomEntity
import com.booking.database.model.MeetingRoomEntity
import com.booking.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInterface {
    suspend fun addUser(user: UserEntity) //not used
    suspend fun addUsers(users: List<UserEntity>)
    suspend fun updateUser(user: UserEntity)
    suspend fun getAllUsers() : List<UserEntity?>
    suspend fun getBookedMeetingRooms() : List<BookedMeetingRoomEntity?>
    suspend fun getBookingsForDate(date: String) : List<BookedMeetingRoomEntity?>
    suspend fun addBookedMeetingRooms(bookedMeetingRooms: List<BookedMeetingRoomEntity>)
    suspend fun updateBookedMeetingRooms(bookedMeetingRoom: BookedMeetingRoomEntity)
    suspend fun getAllMeetingRooms() : List<MeetingRoomEntity?>
    suspend fun addMeetingRooms(meetingRooms: List<MeetingRoomEntity>)
    suspend fun updateMeetingRoom(meetingRoom: MeetingRoomEntity)
    suspend fun getUserByEmail(userEmail: String) : UserEntity?
    suspend fun searchUsers(userEmail: String) : List<UserEntity?>
    suspend fun getMeetingRoomByID(meetingRoomID: String) : MeetingRoomEntity?
    suspend fun getAvailableMeetingRooms(date: String, fromTime: String, toTime: String): List<MeetingRoomEntity?>

}