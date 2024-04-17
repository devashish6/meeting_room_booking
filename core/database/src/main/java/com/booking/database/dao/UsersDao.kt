package com.booking.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.booking.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: UserEntity)

    @Query("SELECT * from users")
    suspend fun getAllUsers() : List<UserEntity>

    @Query("SELECT * FROM users WHERE id = :key")
    suspend fun getUserByID(key: String) : UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUsers(users: List<UserEntity>)

}