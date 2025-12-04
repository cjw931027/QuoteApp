package com.example.quoteapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 確保 DataManager 已初始化
        DataManager.init(context)

        // 隨機選一則名言
        val quotes = DataManager.quotes
        if (quotes.isEmpty()) return

        val randomQuote = quotes.random()

        // 發送通知
        showNotification(context, randomQuote.text, randomQuote.author)

        // 重新排程下一次通知 (針對某些 Android 版本 AlarmManager 不一定會自動重複)
        // 這裡為了簡單使用 setRepeating 或是 AlarmManager 重複機制，
        // 但如果需要精確控制，通常建議在這裡再次呼叫 NotificationScheduler.schedule(...)
    }

    private fun showNotification(context: Context, quoteText: String, author: String) {
        val channelId = "daily_quote_channel"
        val notificationId = 1001

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 建立 Notification Channel (Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "每日名言通知",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "每天發送一則精選名言"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 點擊通知開啟 App
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 建構通知內容
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification) // 確保這個資源存在
            .setContentTitle("每日名言")
            .setContentText("$quoteText — $author")
            .setStyle(NotificationCompat.BigTextStyle().bigText("$quoteText\n\n— $author"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}