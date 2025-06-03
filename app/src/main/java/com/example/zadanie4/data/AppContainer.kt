package com.example.zadanie4.data

import android.content.Context
import com.example.zadanie4.data.dataStore.UserPreferencesDataSource
import com.example.zadanie4.data.network.JsonPlaceholderService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val postRepository: PostRepository
    val todoRepository: TodoRepository
    val userRepository: UserRepository
    val userPreferencesRepository: UserPreferencesRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer{
    private val jsonPlaceholderApiBaseUrl = "https://jsonplaceholder.typicode.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(jsonPlaceholderApiBaseUrl)
        .build()

    private val jsonPlaceholderService: JsonPlaceholderService by lazy {
        retrofit.create(JsonPlaceholderService::class.java)
    }

    private val userPreferencesDataSource by lazy {
        UserPreferencesDataSource(context)
    }

    override val postRepository: PostRepository by lazy {
        PostRepositoryImpl(jsonPlaceholderService)
    }

    override val todoRepository: TodoRepository by lazy {
        TodoRepositoryImpl(jsonPlaceholderService)
    }

    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(jsonPlaceholderService)
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepositoryImpl(userPreferencesDataSource)
    }
}