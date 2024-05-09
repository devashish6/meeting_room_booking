package com.booking.datastore.di

interface UserDataStore {
    suspend fun saveUserCredentials(username: String, password: String)
    suspend fun getUserCredentials(): Pair<String?, String?>
}