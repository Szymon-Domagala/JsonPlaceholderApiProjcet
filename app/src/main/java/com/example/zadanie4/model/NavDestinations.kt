package com.example.zadanie4.model

import kotlinx.serialization.Serializable

@Serializable
object PostListScreen

@Serializable
data class PostDetailsScreen(val postId: Int)

@Serializable
data class UserDetailsScreen(val userId: Int)


