package com.booking.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity (
    @PrimaryKey val id : String,
    val name : String,
    val email : String)