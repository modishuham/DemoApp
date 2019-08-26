package com.my.reminderx

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.DatePicker
import android.app.DatePickerDialog
import java.util.*
import android.widget.TimePicker
import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import java.util.concurrent.TimeUnit
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val componentName = ComponentName(this, ReminderService::class.java)
        /*val jobInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            JobInfo.Builder(100,componentName).setMinimumLatency(9000)
            JobInfo.Builder(101,componentName).setMinimumLatency(14000)
        } else{
            JobInfo.Builder(100,componentName).setPeriodic(5000)
        }*/

        //val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        //jobScheduler.schedule(job1(componentName))
       // jobScheduler.schedule(job2(componentName))

        btn_sdate.setOnClickListener { selectDate() }
        btn_stime.setOnClickListener { selectTime() }
        btn_set.setOnClickListener { setReminder() }
    }

    private fun job1(componentName: ComponentName):JobInfo {
        val bundle = PersistableBundle()
        bundle.putString("name", "shubham")
        val jobInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            JobInfo.Builder(100,componentName).setMinimumLatency(9000).setExtras(bundle)
        } else{
            JobInfo.Builder(100,componentName).setPeriodic(5000)
        }
        return jobInfo.build()
    }

    private fun job2(componentName: ComponentName):JobInfo {
        val bundle = PersistableBundle()
        bundle.putString("name", "modi")
        val jobInfo2 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            JobInfo.Builder(101,componentName).setMinimumLatency(14000).setExtras(bundle)
        } else{
            JobInfo.Builder(100,componentName).setPeriodic(5000)
        }
        return jobInfo2.build()
    }

    private fun selectDate() {
        // Get Current Date
        val calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                tv_sdate.text = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                mYear=year
                mMonth=monthOfYear
                mDay=dayOfMonth
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun selectTime() {
        val c = Calendar.getInstance()
        mHour = c.get(Calendar.HOUR_OF_DAY)
        mMinute = c.get(Calendar.MINUTE)

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                tv_stime.text = "$hourOfDay:$minute"
                mHour=hourOfDay
                mMinute=minute
            }, mHour, mMinute, false
        )
        timePickerDialog.show()
    }

    private fun setReminder() {
        Toast.makeText(this,"Reminder Set",Toast.LENGTH_LONG).show()
        val cal:Calendar = GregorianCalendar(mYear,mMonth,mDay,mHour,mMinute)
        var time = cal.timeInMillis
        Log.e("time",""+time)

        val calendar = Calendar.getInstance()
        val mYear2 = calendar.get(Calendar.YEAR)
        val mMonth2 = calendar.get(Calendar.MONTH)
        val mDay2 = calendar.get(Calendar.DAY_OF_MONTH)

        if (Calendar.getInstance().after(cal)) {
            Toast.makeText(this,"Date is outdated",Toast.LENGTH_SHORT).show()
            return
        }

        if(mYear2==mYear && mMonth2==mMonth && mDay2==mDay) {
            time = cal.timeInMillis - calendar.timeInMillis
            Log.e("time is date same",""+time)
        }

        val bundle = PersistableBundle()
        bundle.putString("name", "shubham")


        val jobId = (1000..9999).random()

        val taskMap:Map<Int,Task> = mutableMapOf()
        val gson = Gson()

        val prvData:String? = AppPref.get("data")
        Log.e("prv",""+prvData.toString())
        if(prvData!!.isEmpty()) {
            taskMap.plus(Pair(jobId, Task("my task descriptio",""+mHour)))
            val data:String = gson.toJson(taskMap)
            AppPref.put("data",data)
        } else {
            val type = object : TypeToken<HashMap<Int, Task>>() {}.type
            val testHashMap2 : Map<Int,Task> = gson.fromJson(prvData,type)
            testHashMap2.plus(Pair(jobId,Task("my task descriptio",""+mHour)))
            val data:String = gson.toJson(testHashMap2)
            Log.e("size",""+testHashMap2.size)
            AppPref.put("data",data)
        }

        val componentName = ComponentName(this, ReminderService::class.java)
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode= jobScheduler.schedule(JobInfo.Builder(jobId,componentName)
            .setMinimumLatency(time)
            .setPersisted(true)
            .setExtras(bundle).build())

    }
}
