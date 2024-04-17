package com.booking.database.di

import android.content.Context
import androidx.room.Room
import com.booking.database.SlotsDataBase
import com.booking.database.com.booking.database.repository.LocalDataSource
import com.booking.database.com.booking.database.repository.LocalDataSourceInterface
import com.booking.database.dao.BookedMeetingRoomDao
import com.booking.database.dao.MeetingRoomDao
import com.booking.database.dao.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context) : SlotsDataBase =
        Room.databaseBuilder(
            context,
            SlotsDataBase::class.java,
            "slots-database"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesUsersDao(slotsDataBase: SlotsDataBase) = slotsDataBase.usersDao()

    @Provides
    @Singleton
    fun providesMeetingRoomDao(slotsDataBase: SlotsDataBase) = slotsDataBase.meetingRooomDao()

    @Provides
    @Singleton
    fun providesBookingDao(slotsDataBase: SlotsDataBase) = slotsDataBase.meetingRoomBooking()

    @Singleton
    @Provides
    fun provideLocalDataSourceInterface (
        bookedMeetingRoomDao: BookedMeetingRoomDao,
        meetingRoomDao: MeetingRoomDao,
        usersDao: UsersDao
    ): LocalDataSourceInterface {
        return LocalDataSource(bookedMeetingRoomDao,
            meetingRoomDao,
            usersDao)
    }
}