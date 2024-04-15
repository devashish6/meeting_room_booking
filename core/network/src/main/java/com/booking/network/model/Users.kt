package com.booking.network.model

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("documents")
    val users: List<User>
)

data class User(
    @SerializedName("name")
    val name: String,
    @SerializedName("fields")
    val fields: Fields,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("updateTime")
    val updateTime: String
)

data class Fields(
    @SerializedName("user_id")
    val userId: UserId,
    @SerializedName("user_name")
    val userName: UserName,
    @SerializedName("user_email")
    val userEmail: UserEmail
)

data class UserId(
    @SerializedName("stringValue")
    val stringValue: String
)

data class UserName(
    @SerializedName("stringValue")
    val stringValue: String
)

data class UserEmail(
    @SerializedName("stringValue")
    val stringValue: String
)

