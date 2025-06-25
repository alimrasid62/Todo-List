package com.alimrasid.simpletodolist.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.alimrasid.simpletodolist.data.entity.Task

object ReminderHelper {
    @SuppressLint("ScheduleExactAlarm")
    fun setReminder(context: Context, task: Task) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TaskReminderReceiver::class.java).apply {
            putExtra("title", task.title)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id, // Gunakan ID unik
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            task.dueDate,
            pendingIntent
        )
    }
}