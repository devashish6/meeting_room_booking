package com.booking.slots

//import com.google.gson.JsonObject

//fun convertToCustomFormat(user: HashMap<String, String>): JsonObject {
//    val output = JsonObject()
//    val fields = JsonObject()
//
//    for ((key, value) in user) {
//        val fieldValue = JsonObject()
//        fieldValue.addProperty("stringValue", value)
//        fields.add(key, fieldValue)
//    }
//
//    output.add("fields", fields)
//
//    return output
//}
//

//fun extractUserInfo(json: String): JSONObject {
//    val jsonObject = JSONObject(json)
//    val name = jsonObject.getString("name")
//    val fields = jsonObject.getJSONObject("fields")
//    val userId = fields.getJSONObject("user_id").getString("stringValue")
//    val userName = fields.getJSONObject("user_name").getString("stringValue")
//    val userEmail = fields.getJSONObject("user_email").getString("stringValue")
//
//    val resultJson = JSONObject()
//    resultJson.put("name", name)
//    resultJson.put("user_id", userId)
//    resultJson.put("user_name", userName)
//    resultJson.put("user_email", userEmail)
//
//    return resultJson
//}

/*
curl --location 'https://firestore.googleapis.com/v1/projects/slots-21fc8/databases/(default)/documents/meeting_room' \
--header 'Content-Type: application/json' \
--header 'access_token: AIzaSyCPteeEMcb8CpOJBFU0T6-byI5RuGRgx04' \
--data '{
    "fields": {
        "location": {
            "stringValue": "Ground Floor"
        },
        "meeting_room_id": {
            "stringValue": "MR_8"
        },
        "meeting_room_size": {
            "integerValue": "20"
        }
    }
}'

curl --location 'https://firestore.googleapis.com/v1/projects/slots-21fc8/databases/(default)/documents/users' \
--header 'Content-Type: application/json' \
--header 'access_token: AIzaSyCPteeEMcb8CpOJBFU0T6-byI5RuGRgx04' \
--data-raw '{
    "fields": {
        "user_email": {
            "stringValue": "devashishmurthyk+999@gmail.com"
        },
        "user_id": {
            "stringValue": "USR_999"
        },
        "user_name": {
            "stringValue": "Devashish"
        }
    }
}'

curl --location 'https://firestore.googleapis.com/v1/projects/slots-21fc8/databases/(default)/documents/meeting_room'

curl --location 'https://firestore.googleapis.com/v1/projects/slots-21fc8/databases/(default)/documents/meeting_room/0ZYVtpcnyvJMAEn6ObDY'

curl --location 'https://firestore.googleapis.com/v1/projects/slots-21fc8/databases/(default)/documents/users'

curl --location 'https://firestore.googleapis.com/v1/projects/slots-21fc8/databases/(default)/documents/users/53LQ33XqboOHtqBbmQQF' \
--data ''
 */