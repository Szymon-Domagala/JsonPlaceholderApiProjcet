package com.example.zadanie4.ui.screens.userDetails

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.zadanie4.PostListApplication
import com.example.zadanie4.data.TodoRepository
import com.example.zadanie4.data.UserRepository
import com.example.zadanie4.model.Todo
import com.example.zadanie4.model.User
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    private val userRepository: UserRepository,
    private val todoRepository: TodoRepository
) : ViewModel() {

    sealed interface UiState {
        data class Success(val user: User, val todos: List<Todo>) : UiState
        object Error : UiState
        object Loading : UiState
    }

    var uiState: UiState by mutableStateOf(UiState.Loading); private set

    fun loadUserAndTodos(userId: Int) {
        viewModelScope.launch {
            uiState = UiState.Loading
            try {
                val user = userRepository.getUserById(userId)
                val todos = todoRepository.getTodosByUserId(userId)
                uiState = UiState.Success(user, todos)
            } catch (e: Exception) {
                uiState = UiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PostListApplication)
                val userRepository = application.container.userRepository
                val todoRepository = application.container.todoRepository
                UserDetailsViewModel(userRepository, todoRepository)
            }
        }
    }
}
