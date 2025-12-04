package com.example.quoteapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationReceiver", "onReceive triggered. Action: ${intent.action}")

        // 1. 初始化資料
        DataManager.init(context)

        // 2. 處理重開機事件
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("NotificationReceiver", "Boot completed, rescheduling...")
            NotificationScheduler.scheduleDailyNotification(context)
            return
        }

        // 3. 顯示通知
        showNotification(context)

        // 4. 排程下一次
        Log.d("NotificationReceiver", "Notification shown, scheduling next one...")
        NotificationScheduler.scheduleDailyNotification(context)
    }

    private fun showNotification(context: Context) {
        val quotes = DataManager.quotes
        if (quotes.isEmpty()) return

        // 隨機抽選一則
        val randomQuote = quotes.random()
        val quoteText = randomQuote.text
        val author = randomQuote.author

        val channelId = "daily_quote_channel"
        val notificationId = 1001

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

        // [關鍵修改] 建立 Intent 時，把名言內容一起放入 extras
        val intent = Intent(context, MainActivity::class.java).apply {
            // 使用 CLEAR_TASK 確保點擊通知是重新開啟 App，避免舊狀態干擾
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notification_quote_text", quoteText)
            putExtra("notification_quote_author", author)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("每日名言")
            .setContentText("$quoteText — $author")
            .setStyle(NotificationCompat.BigTextStyle().bigText("$quoteText\n\n— $author"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}