package com.booking.database.di

import SlotsDataBase
import android.content.Context
import androidx.room.Room
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
    fun providesDatabase (@ApplicationContext context: Context) : SlotsDataBase =
        Room.databaseBuilder(
            context,
            SlotsDataBase::class.java,
            "slots-database"
        ).build()
}