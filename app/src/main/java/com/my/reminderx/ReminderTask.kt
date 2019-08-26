package com.my.reminderx

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast

open class ReminderTask: AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg args: Unit?) {
        //Toast.makeText(ReminderXApp.applicationContext(),"task start",Toast.LENGTH_LONG).show()
        Log.e("task","task start in background")
    }
}