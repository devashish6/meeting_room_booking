package com.booking.data.model

import com.booking.database.model.UserEntity
import com.booking.model.model.User
import com.booking.network.model.FirebaseDocument
import com.booking.network.model.FirebaseResponseFields
import com.booking.network.model.RemoteUser

fun RemoteUser.asEntity() = UserEntity(
//    id = userId,
    email = getUserEmail(),
    name = getUserName(),
    password = getUserPassword()
)
fun UserEntity.asDomainModel() = User(
//    id = id,
    email = email,
    name = name,
    password = password
)
/*
    1. We have firebase documents
    2. we need to extract user details from each firebase document
    3. convert the whole to List of UserEntity
 */

fun FirebaseDocument<RemoteUser>.asListOfUserEntity(): List<UserEntity> {
    return documents.map { firebaseResponseFields ->
        firebaseResponseFields.fields.asEntity()
    }
}
//fun List<UserEntity>.asListOfUser(): List<User> {
//
//}