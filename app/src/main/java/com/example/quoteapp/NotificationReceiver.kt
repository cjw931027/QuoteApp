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

        DataManager.init(context)

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("NotificationReceiver", "Boot completed, rescheduling...")
            NotificationScheduler.scheduleDailyNotification(context)
            return
        }

        showNotification(context)

        Log.d("NotificationReceiver", "Notification shown, scheduling next one...")
        NotificationScheduler.scheduleDailyNotification(context)
    }

    private fun showNotification(context: Context) {
        val quotes = DataManager.quotes
        if (quotes.isEmpty()) return

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

        // [修改] 建立 Explicit Intent 指向 MainActivity
        val intent = Intent(context, MainActivity::class.java).apply {
            // [關鍵] 加入這兩個 Flags：
            // NEW_TASK: 啟動新的 Task
            // CLEAR_TASK: 清除該 Task 上所有現存 Activity，確保 MainActivity 是重新建立的
            // 這樣 HomeFragment 就一定會重新執行 onViewCreated
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // 放入資料
            putExtra("notification_quote_text", quoteText)
            putExtra("notification_quote_author", author)
        }

        // 使用唯一 ID 建立 PendingIntent
        val uniqueRequestCode = System.currentTimeMillis().toInt()

        val pendingIntent = PendingIntent.getActivity(
            context,
            uniqueRequestCode,
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