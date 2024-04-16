package com.booking.network.model

import com.google.gson.annotations.SerializedName

data class RemoteUser(
    @SerializedName("user_id")
    var userId: StringValue,
    @SerializedName("user_name")
    val userName: StringValue,
    @SerializedName("user_email")
    val userEmail: StringValue
) {
    fun getUserId() : String {
        return userId.stringValue
    }
    fun setUserId(encryptedUserId: String) {
        userId.stringValue = encryptedUserId
    }
    fun getUserName() : String {
        return userName.stringValue
    }
    fun getUserEmail() : String {
        return userEmail.stringValue
    }

}

