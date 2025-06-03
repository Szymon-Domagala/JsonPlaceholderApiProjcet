package com.example.zadanie4.data.dataStore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.zadanie4.model.UserPrefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.booleanPreferencesKey

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesDataSource(private val context: Context) {

    companion object {
        private val FIRST_NAME = stringPreferencesKey("first_name")
        private val LAST_NAME = stringPreferencesKey("last_name")
        private val PROFILE_IMAGE_PATH = stringPreferencesKey("profile_image_path")
        private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    }

    val userFlow: Flow<UserPrefs> = context.dataStore.data.map { preferences ->
        UserPrefs(
            firstName = preferences[FIRST_NAME] ?: "",
            lastName = preferences[LAST_NAME] ?: "",
            profileImagePath = preferences[PROFILE_IMAGE_PATH] ?: "",
            isDarkTheme = preferences[IS_DARK_THEME] ?: false
        )
    }

    suspend fun updateUser(firstName: String, lastName: String, imagePath: String) {
        context.dataStore.edit { preferences ->
            preferences[FIRST_NAME] = firstName
            preferences[LAST_NAME] = lastName
            preferences[PROFILE_IMAGE_PATH] = imagePath
        }
    }

    suspend fun updateDarkTheme(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = enabled
        }
    }
}