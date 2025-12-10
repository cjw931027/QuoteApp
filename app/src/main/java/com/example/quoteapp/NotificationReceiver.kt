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

        // 儲存選到的名言
        DataManager.saveNotificationQuote(context, randomQuote)

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

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("from_notification", true)
        }

        val uniqueRequestCode = System.currentTimeMillis().toInt()

        val pendingIntent = PendingIntent.getActivity(
            context,
            uniqueRequestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // [修改 4] 將通知內容改為 "快來查看今日名言!"
        // 原本的詳細內容依然保留在 BigTextStyle (展開後可見)，或是使用者點擊後進入 App 觀看
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("每日名言")
            .setContentText("快來查看今日名言!") // 這裡修改了顯示文字
            .setStyle(NotificationCompat.BigTextStyle().bigText("$quoteText\n\n— $author")) // 展開後還是可以看到完整內容
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}