package com.example.quoteapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Calendar

object NotificationScheduler {

    private const val REQUEST_CODE = 1001

    fun scheduleDailyNotification(context: Context) {
        // 1. 如果通知被關閉，直接取消並返回
        if (!DataManager.isNotificationEnabled) {
            cancelNotification(context)
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)

        // 加入 FLAG_IMMUTABLE (Android 12+ 規定)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 2. 設定觸發時間
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, DataManager.notificationHour)
            set(Calendar.MINUTE, DataManager.notificationMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // 3. 關鍵修正：確保設定的時間是「未來」
        // 如果設定的時間比現在早 (例如現在 10:00，設定 09:00)，就代表是「明天」的 09:00
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        Log.d("NotificationScheduler", "Alarm scheduled for: ${calendar.time}")

        // 4. 改用 setExactAndAllowWhileIdle 取代 setRepeating
        // 這能解決「一設定若是過去時間就馬上響」的問題，也能解決 Doze 模式下不準時的問題
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            Log.e("NotificationScheduler", "Permission error: ${e.message}")
            e.printStackTrace()
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