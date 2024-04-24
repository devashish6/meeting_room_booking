package com.booking.network.utils

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

fun convertToFormat(hashMap: HashMap<String, String>): HashMap<String, String> {
    var output = HashMap<String, String>()
    val fields = JsonObject()

    for ((key, value) in hashMap) {
        val fieldValue = JsonObject()
        fieldValue.addProperty("stringValue", value)
        fields.add(key, fieldValue)
    }

    output["fields"] = fields.toString()

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
                fieldsObject.add(entry.key, JsonObject().apply { add("arrayValue", arrayValueObject) })
            }
            else -> {
                val stringValueObject = JsonObject()
                stringValueObject.addProperty("stringValue", entry.value.asString)
                fieldsObject.add(entry.key, JsonObject().apply { add("stringValue", stringValueObject) })
            }
        }
    }

    val finalJsonObject = JsonObject()
    finalJsonObject.add("fields", fieldsObject)

    return finalJsonObject
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