package com.my.reminderx

import android.app.Application
import android.content.Context

class ReminderXApp: Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: ReminderXApp? = null

        fun applicationContext() : Context {
            return (instance as ReminderXApp).applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}