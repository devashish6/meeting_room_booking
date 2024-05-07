package com.booking.datastore.di
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.stringPreferencesKey
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.flow.first
//import javax.inject.Inject
//
//class PreferenceUserDataStore @Inject constructor(
//    @ApplicationContext context: Context,
//    private val userPreferences: DataStore<UserPreferences>
//) : UserDataStore, DataStore<Preferences> {
//
//
//    override suspend fun saveUserCredentials(username: String, password: String) {
//        dataStore.edit { preferences ->
//            preferences[USERNAME_KEY] = username
//            preferences[PASSWORD_KEY] = password
//        }
//    }
//
//    override suspend fun getUserCredentials(): Pair<String?, String?> {
//        val preferences = dataStore.data.first()
//        val username = preferences[USERNAME_KEY]
//        val password = preferences[PASSWORD_KEY]
//        return Pair(username, password)
//    }
//
//    companion object {
//        private val USERNAME_KEY = stringPreferencesKey("username")
//        private val PASSWORD_KEY = stringPreferencesKey("password")
//    }
//}