package com.example.zadanie4.model

import kotlinx.serialization.Serializable

@Serializable
data class Geo(
    val lat: String,
    val lng: String)
