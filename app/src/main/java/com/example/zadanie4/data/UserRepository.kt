package com.example.zadanie4.data

import com.example.zadanie4.data.network.JsonPlaceholderService
import com.example.zadanie4.model.User

interface UserRepository {
    suspend fun getUserById(userId: Int): User
}

class UserRepositoryImpl(
    private val jsonPlaceholderService: JsonPlaceholderService
) : UserRepository {
    override suspend fun getUserById(userId: Int): User = jsonPlaceholderService.getUserById(userId)
}
