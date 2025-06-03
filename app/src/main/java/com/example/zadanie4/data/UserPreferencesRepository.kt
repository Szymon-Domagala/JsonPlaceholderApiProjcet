package com.example.zadanie4.data

import com.example.zadanie4.data.dataStore.UserPreferencesDataSource
import com.example.zadanie4.model.UserPrefs
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userFlow: Flow<UserPrefs>
    suspend fun updateUser(firstName: String, lastName: String, imagePath: String)
    suspend fun updateDarkTheme(enabled: Boolean)
}


class UserPreferencesRepositoryImpl(
    private val dataSource: UserPreferencesDataSource
) : UserPreferencesRepository {

    override val userFlow: Flow<UserPrefs> = dataSource.userFlow

    override suspend fun updateUser(firstName: String, lastName: String, imagePath: String) {
        dataSource.updateUser(firstName, lastName, imagePath)
    }

    override suspend fun updateDarkTheme(enabled: Boolean) {
        dataSource.updateDarkTheme(enabled)
    }
}