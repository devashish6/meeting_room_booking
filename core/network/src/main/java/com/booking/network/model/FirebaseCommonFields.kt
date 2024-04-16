package com.booking.network.model

import com.google.gson.annotations.SerializedName


data class FirebaseDocument<T>(
    @SerializedName("documents")
    val documents: List<FirebaseResponseFields<T>>
)

data class FirebaseResponseFields<T> (
    @SerializedName("name")
    val name: String,
    @SerializedName("fields")
    val fields: T,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("updateTime")
    val updateTime: String
)

data class StringValue(
    @SerializedName("stringValue")
    var stringValue: String
)

data class IntegerValue(
    @SerializedName("integerValue")
    val integerValue: String
)

data class ArrayValue(
    @SerializedName("arrayValue")
    val arrayValue: List<String>
)