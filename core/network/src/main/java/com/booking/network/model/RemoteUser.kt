package com.booking.network.model

import com.google.gson.annotations.SerializedName

data class RemoteUser(
    var userId: String,
    @SerializedName("user_name")
    val userName: StringValue,
    @SerializedName("user_email")
    val userEmail: StringValue,
    @SerializedName("user_password")
    val userPassword: StringValue
) {
//    fun getUserId() : String {
//        return userId
//    }
//    fun setUserId(encryptedUserId: String) {
//        userId = encryptedUserId
//    }
    fun getUserName() : String {
        return userName.stringValue
    }
    fun getUserEmail() : String {
        return userEmail.stringValue
    }

    fun getUserPassword() : String {
        return userPassword.stringValue
    }
}

