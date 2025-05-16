package com.example.zadanie4.ui.screens.postDetails

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.zadanie4.PostListApplication
import com.example.zadanie4.data.PostRepository
import com.example.zadanie4.data.UserRepository
import com.example.zadanie4.model.Post
import com.example.zadanie4.model.User
import kotlinx.coroutines.launch

class PostDetailsViewModel(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    sealed interface UiState {
        data class Success(val post: Post, val user: User) : UiState
        object Error : UiState
        object Loading : UiState
    }

    var uiState: UiState by mutableStateOf(UiState.Loading); private set



    fun loadPost(postId: Int) {
        viewModelScope.launch {
            uiState = UiState.Loading
            try {
                val post = postRepository.getPostById(postId)
                val user = userRepository.getUserById(post.userId)
                uiState = UiState.Success(post, user)
            } catch (e: Exception) {
                uiState = UiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PostListApplication)
                val postRepository = application.container.postRepository
                val userRepository = application.container.userRepository
                PostDetailsViewModel(postRepository, userRepository)
            }
        }
    }
}
