package com.mooc.ppjoke

import android.app.Application
import com.mooc.libnetwork.ApiService

class JokeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ApiService.initiate("http://123.56.232.18:8080/serverdemo", null)
    }
}