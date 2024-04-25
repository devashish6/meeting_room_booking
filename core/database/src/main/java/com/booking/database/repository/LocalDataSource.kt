package com.booking.database.com.booking.database.repository

import com.booking.database.dao.BookedMeetingRoomDao
import com.booking.database.dao.MeetingRoomDao
import com.booking.database.dao.UsersDao
import com.booking.database.model.BookedMeetingRoomEntity
import com.booking.database.model.MeetingRoomEntity
import com.booking.database.model.UserEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val bookingMeetingRoomDao: BookedMeetingRoomDao,
    private val meetingRoomDao: MeetingRoomDao,
    private val usersDao: UsersDao
) : LocalDataSourceInterface {
    override suspend fun addUser(user: UserEntity) {
        usersDao.addUser(user)
    }

    override suspend fun addUsers(users: List<UserEntity>) {
        usersDao.addUsers(users)
    }

    override suspend fun getAllUsers(): List<UserEntity?> {
        return usersDao.getAllUsers()
    }

    override suspend fun getUserByEmail(userEmail: String): UserEntity? {
        return usersDao.getUserByEmail(userEmail)
    }

    override suspend fun getAllMeetingRooms(): List<MeetingRoomEntity?> {
        return meetingRoomDao.getAllMeetingRooms()
    }

    override suspend fun addMeetingRooms(meetingRooms: List<MeetingRoomEntity>) {
        return meetingRoomDao.addMeetingRooms(meetingRooms)
    }

    override suspend fun getMeetingRoomByID(meetingRoomID: String): MeetingRoomEntity? {
        return meetingRoomDao.getMeetingRoomByID(meetingRoomID)
    }

    override suspend fun getBookedMeetingRooms(): List<BookedMeetingRoomEntity?> {
        return bookingMeetingRoomDao.getBookedMeetingRooms()
    }

    override suspend fun getBookingsForDate(date: String): List<BookedMeetingRoomEntity?> {
        return bookingMeetingRoomDao.getBookingsForDate(date)
    }

    override suspend fun addBookedMeetingRooms(bookedMeetingRooms: List<BookedMeetingRoomEntity>) {
        return bookingMeetingRoomDao.addBookedMeetingRooms(bookedMeetingRooms)
    }

}