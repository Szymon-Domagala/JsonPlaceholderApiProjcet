package com.example.zadanie4.ui.screens.postList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

class PostListViewModel(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
)  : ViewModel(){

    data class PostWithUser(
        val post: Post,
        val user: User?
    )

    sealed interface UiState {
        data class Success(val posts: List<PostWithUser>): UiState
        object Error: UiState
        object Loading: UiState
    }

    var uiState: UiState by mutableStateOf(UiState.Loading); private set

    init {
        loadPostsWithUsers()
    }

    fun loadPostsWithUsers(){
        viewModelScope.launch{
            uiState = UiState.Loading
            uiState = try{
                val posts = postRepository.getAllPosts()

                val usersMap: Map<Int, User> = posts
                    .map { it.userId }
                    .distinct()
                    .associateWith { userRepository.getUserById(it) }

                val combined = posts.map { post ->
                    PostWithUser(post = post, user = usersMap[post.userId])
                }

                UiState.Success(combined)
            } catch (e: Exception) {
                UiState.Error
            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PostListApplication)
                val postRepository = application.container.postRepository
                val userRepository = application.container.userRepository
                PostListViewModel(postRepository, userRepository)
            }
        }
    }
}