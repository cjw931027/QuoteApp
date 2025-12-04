package com.example.quoteapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.Calendar

object NotificationScheduler {

    private const val REQUEST_CODE = 1001

    fun scheduleDailyNotification(context: Context) {
        // 如果通知被關閉，直接取消
        if (!DataManager.isNotificationEnabled) {
            cancelNotification(context)
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 設定觸發時間
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, DataManager.notificationHour)
            set(Calendar.MINUTE, DataManager.notificationMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // 如果設定的時間已經過了，就設為明天
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // 設定重複鬧鐘 (每天一次)
        // 注意：setRepeating 在 Android 6.0+ 的 Doze 模式下可能不準確
        // 如果需要非常準確的時間，需改用 setExactAndAllowWhileIdle 並在 Receiver 中手動設定下一次
        // 這裡使用 setRepeating 適合一般用途
        try {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            Log.d("NotificationScheduler", "Scheduled for ${calendar.time}")
        } catch (e: SecurityException) {
            e.printStackTrace()
            // Android 12+ 需要 SCHEDULE_EXACT_ALARM 權限才能設定精確鬧鐘，
            // 這裡用 setRepeating 不需要該權限，但若改用 setExact 則需要處理。
        }
    }

    fun cancelNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
        Log.d("NotificationScheduler", "Notification Canceled")
    }
}