package com.my.reminderx

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import android.widget.Toast

class ReminderService: JobService() {

    private lateinit var mTask: ReminderTask

    override fun onStartJob(jobParams: JobParameters?): Boolean {
        mTask = object : ReminderTask(){
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                jobFinished(jobParams,false)
                val intent = Intent(ReminderXApp.applicationContext(),TaskReceiver::class.java)
                intent.action = "com.reminderX.taskExecute"
                intent.putExtra("name",jobParams?.extras?.getString("name"))
                sendBroadcast(intent)
                //Toast.makeText(ReminderXApp.applicationContext(),"complete",Toast.LENGTH_LONG).show()
            }
        }
        mTask.execute()
        return false
    }

    override fun onStopJob(jobParams: JobParameters?): Boolean {
        mTask.cancel(false)
        return false
    }
}