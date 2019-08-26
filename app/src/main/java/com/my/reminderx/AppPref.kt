package com.my.reminderx

import android.content.Context
import android.content.SharedPreferences

object AppPref {

    private var mSharedPreferences: SharedPreferences = ReminderXApp.applicationContext()
        .getSharedPreferences("ReminderX", Context.MODE_PRIVATE)
    private lateinit var mEditor: SharedPreferences.Editor

    fun put(key: String, value: String) {
        mEditor = mSharedPreferences.edit()
        mEditor.putString(key, value).apply()
    }

    fun get(key: String): String? {
        return mSharedPreferences.getString(key, "")
    }
}