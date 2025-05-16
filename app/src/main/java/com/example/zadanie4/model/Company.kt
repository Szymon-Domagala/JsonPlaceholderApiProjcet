package com.example.zadanie4.model

import kotlinx.serialization.Serializable

@Serializable
data class Company(
    val name: String,
    val catchPhrase: String,
    val bs: String
)
