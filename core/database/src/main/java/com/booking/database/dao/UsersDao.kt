package com.booking.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.booking.database.model.UserEntity

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: UserEntity)

    @Query("SELECT * from users ORDER BY email ASC")
    suspend fun getAllUsers() : List<UserEntity?>

    @Query("SELECT * FROM users WHERE email = :key")
    suspend fun getUserByEmail(key: String) : UserEntity?

    @Query("SELECT * FROM users WHERE email LIKE '%' || :key || '%' ORDER BY email ASC")
    suspend fun searchUsers(key: String) : List<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUsers(users: List<UserEntity>)

}