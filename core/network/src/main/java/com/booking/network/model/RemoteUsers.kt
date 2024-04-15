package com.booking.network.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id")
    val userId: StringValue,
    @SerializedName("user_name")
    val userName: StringValue,
    @SerializedName("user_email")
    val userEmail: StringValue
)

