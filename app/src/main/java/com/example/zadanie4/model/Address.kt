package com.example.zadanie4.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: Geo
)
