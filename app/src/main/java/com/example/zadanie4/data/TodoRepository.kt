package com.example.zadanie4.data

import com.example.zadanie4.data.network.JsonPlaceholderService
import com.example.zadanie4.model.Todo

interface TodoRepository {
    suspend fun getTodosByUserId(userId: Int): List<Todo>
}

class TodoRepositoryImpl(
    private val jsonPlaceholderService: JsonPlaceholderService
) : TodoRepository {
    override suspend fun getTodosByUserId(userId: Int): List<Todo> = jsonPlaceholderService.getTodosByUserId(userId)
}

