package com.example.zadanie4

import android.app.Application
import com.example.zadanie4.data.AppContainer
import com.example.zadanie4.data.DefaultAppContainer

class PostListApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}