package com.example.zadanie4.ui.screens.myProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.zadanie4.PostListApplication
import com.example.zadanie4.data.UserPreferencesRepository
import com.example.zadanie4.model.UserPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyProfileViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    sealed interface UiState {
        data class Success(val userPrefs: UserPrefs) : UiState
        object Loading : UiState
        object Error : UiState
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadUserPrefs()
    }

    private fun loadUserPrefs() {
        viewModelScope.launch {
            try {
                userPreferencesRepository.userFlow.collectLatest {
                    _uiState.value = UiState.Success(it)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error
            }
        }
    }

    fun updateUser(firstName: String, lastName: String, imagePath: String) {
        viewModelScope.launch {
            try {
                userPreferencesRepository.updateUser(firstName, lastName, imagePath)
            } catch (e: Exception) {
                _uiState.value = UiState.Error
            }
        }
    }

    fun updateDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateDarkTheme(enabled)
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PostListApplication)
                val userPreferencesRepository = application.container.userPreferencesRepository
                MyProfileViewModel(userPreferencesRepository)
            }
        }
    }
}