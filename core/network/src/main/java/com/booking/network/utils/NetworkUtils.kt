package com.booking.network.utils

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject

fun convertToCustomFormat(hashMap: HashMap<String, String>): JsonObject {
    val output = JsonObject()
    val fields = JsonObject()

    for ((key, value) in hashMap) {
        val fieldValue = JsonObject()
        fieldValue.addProperty("stringValue", value)
        fields.add(key, fieldValue)
    }

    output.add("fields", fields)

    return output
}

fun convertToFormat(hashMap: HashMap<String, Any>): JsonObject {
    val output = JsonObject()
    val fields = JsonObject()

    for ((key, values) in hashMap) {
        val fieldValue = JsonObject()
        if (values is String) {
            fieldValue.addProperty("stringValue", values)
        } else if (values is List<*>) {
            val arrayObject = JsonObject()
            val jsonArray = JsonArray()
            values.forEach {
                val stringValueObject = JsonObject() // Create a new instance for each item
                stringValueObject.addProperty("stringValue", it.toString())
                jsonArray.add(stringValueObject)
            }
            arrayObject.add("values", jsonArray)
            fieldValue.add("arrayValue", arrayObject)
        }
        fields.add(key, fieldValue)
    }

    output.add("fields", fields)

    return output
}


fun convertToJsonWithFields(jsonObject: JsonObject): JsonObject {
    val fieldsObject = JsonObject()

    jsonObject.entrySet().forEach { entry ->
        when (entry.key) {
            "attendees" -> {
                val attendeesArray = JsonArray()
                entry.value.asJsonArray.forEach { attendee ->
                    val stringValueObject = JsonObject()
                    stringValueObject.addProperty("stringValue", attendee.asString)
                    attendeesArray.add(stringValueObject)
                }
                val arrayValueObject = JsonObject()
                arrayValueObject.add("values", attendeesArray)
                fieldsObject.add(
                    entry.key,
                    JsonObject().apply { add("arrayValue", arrayValueObject) })
            }

            else -> {
                val stringValueObject = JsonObject()
                stringValueObject.addProperty("stringValue", entry.value.asString)
                fieldsObject.add(
                    entry.key,
                    JsonObject().apply { add("stringValue", stringValueObject) })
            }
        }
    }

    val finalJsonObject = JsonObject()
    finalJsonObject.add("fields", fieldsObject)

    return finalJsonObject
}

fun convertToDesiredFormat(input: HashMap<String, Any>): HashMap<String, Any> {
    val output = HashMap<String, Any>()

    val fields = mutableMapOf<String, Any>()
    fields["from_time"] = mapOf("stringValue" to "10")
    fields["to_time"] = mapOf("stringValue" to "11")
    fields["meeting_room_id"] = mapOf("stringValue" to "8x5SrreVo1taY2xkYzPt")
    fields["host"] = mapOf("stringValue" to "devashishmurthyk@gmail.com")
    fields["date"] = mapOf("stringValue" to "2024-05-01")
    fields["meeting_title"] = mapOf("stringValue" to "Technical Round 1")
    val attendeesArray = mutableListOf<Map<String, Map<String, String>>>()
    (1..2).forEach { i ->
        val attendee = mapOf("stringValue" to "devashishmurthyk+$i@gmail.com")
        attendeesArray.add(mapOf("stringValue" to attendee))
    }
    fields["attendees"] = mapOf("arrayValue" to mapOf("values" to attendeesArray))

    output["fields"] = fields

    return output

//    val gson = Gson()
//    val jsonString = gson.toJson(output)
//
//    (output as Map<*, *>?)?.let { JSONObject(it).toString() }
//
//
//
//    return (output as Map<*, *>?)?.let { JSONObject(it) }
}


//fun extractUserInfo(user: String): User {
//    val jsonObject = JSONObject(user)
//    val name = jsonObject.getString("name")
//    val fields = jsonObject.getJSONObject("fields")
//    val userId = extractID(name)
//    val userName = fields.getJSONObject("user_name").getString("stringValue")
//    val userEmail = fields.getJSONObject("user_email").getString("stringValue")
//
//    return User(userID = userId, name = userName, email = userEmail)
//}

//fun extraMeetingInfo(json: String): MeetingRoom {
//    val jsonObject = JSONObject(json)
//    val name = jsonObject.getString("name")
//    val fields = jsonObject.getJSONObject("fields")
//    val meetingRoomID = extractID(name)
//    val meetingRoomSize = fields.getJSONObject("meeting_room_size").getString("stringValue")
//    val location = fields.getJSONObject("location").getString("stringValue")
//
//    return MeetingRoom(meetingRoomID = meetingRoomID, meetingRoomSize = meetingRoomSize, location = location)
//}