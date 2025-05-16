package com.example.zadanie4.data.network

import com.example.zadanie4.model.Post
import com.example.zadanie4.model.Todo
import com.example.zadanie4.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceholderService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): Post

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User

    @GET("users/{id}/todos")
    suspend fun getTodosByUserId(@Path("id") id: Int): List<Todo>
}