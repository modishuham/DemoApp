package com.my.reminderx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class TaskReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.action.equals("com.reminderX.taskExecute")) {
            Toast.makeText(context,"hello",Toast.LENGTH_LONG).show()
            val name:String?=intent.getStringExtra("name")
            Log.e("task","broadcast receive"+name)
        }
    }
}