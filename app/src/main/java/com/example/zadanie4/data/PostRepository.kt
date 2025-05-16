package com.example.zadanie4.data

import com.example.zadanie4.data.network.JsonPlaceholderService
import com.example.zadanie4.model.Post

interface PostRepository {
    suspend fun getAllPosts(): List<Post>
    suspend fun getPostById(postId: Int): Post
}

class PostRepositoryImpl(
    private val jsonPlaceholderService: JsonPlaceholderService
): PostRepository{
    override suspend fun getAllPosts(): List<Post> = jsonPlaceholderService.getPosts()
    override suspend fun getPostById(postId: Int): Post = jsonPlaceholderService.getPostById(postId)
}