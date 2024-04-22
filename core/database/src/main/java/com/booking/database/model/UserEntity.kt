package com.booking.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity (
    val id : String,
    @PrimaryKey val email : String,
    val name : String,
    val password: String)