package com.example.zadanie4.model

data class UserPrefs(
    val firstName: String,
    val lastName: String,
    val profileImagePath: String,
    val isDarkTheme: Boolean = false
)