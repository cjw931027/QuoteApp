package com.example.quoteapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var lastQuote: Quote? = null
    private lateinit var textQuote: TextView
    private lateinit var textAuthor: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textQuote = view.findViewById(R.id.text_quote)
        textAuthor = view.findViewById(R.id.text_author)
        val btnRefresh = view.findViewById<Button>(R.id.btn_refresh)
        val btnShare = view.findViewById<View>(R.id.btn_share)

        // 內部函式：隨機更新名言
        fun updateRandomQuote() {
            val allQuotes = DataManager.quotes
            if (allQuotes.isEmpty()) return

            if (allQuotes.size == 1) {
                val q = allQuotes[0]
                textQuote.text = q.text
                textAuthor.text = "— ${q.author}"
                lastQuote = q
                return
            }

            var newQuote: Quote
            do {
                newQuote = allQuotes.random()
            } while (newQuote == lastQuote)

            textQuote.text = newQuote.text
            textAuthor.text = "— ${newQuote.author}"
            lastQuote = newQuote
        }

        btnRefresh.setOnClickListener { updateRandomQuote() }

        btnShare.setOnClickListener {
            val quoteContent = textQuote.text.toString()
            val quoteAuthor = textAuthor.text.toString()
            val shareText = "$quoteContent\n$quoteAuthor\n\n- 來自 QuoteApp"

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "分享名言")
            startActivity(shareIntent)
        }

        // [關鍵邏輯修正] 檢查 Intent 資料
        val intent = requireActivity().intent
        val notifText = intent.getStringExtra("notification_quote_text")
        val notifAuthor = intent.getStringExtra("notification_quote_author")

        Log.d("HomeFragment", "Checking Intent: text=$notifText, author=$notifAuthor")

        if (!notifText.isNullOrEmpty() && !notifAuthor.isNullOrEmpty()) {
            // [情況 A] 從通知進入：顯示指定名言
            Log.d("HomeFragment", "Displaying notification quote")
            textQuote.text = notifText
            textAuthor.text = "— $notifAuthor"

            // 設定 lastQuote，避免按重抽時剛好又抽到這句會沒反應 (雖然機率低)
            lastQuote = Quote(text = notifText, author = notifAuthor, category = "Notification")

            // [重要] 移除 Extra，避免旋轉螢幕或下次打開 App 時又重複顯示通知內容
            // 注意：因為我們在 NotificationReceiver 用了 CLEAR_TASK，這裡移除主要防止螢幕旋轉
            intent.removeExtra("notification_quote_text")
            intent.removeExtra("notification_quote_author")
        } else {
            // [情況 B] 一般進入：如果畫面是第一次建立 (savedInstanceState == null)，才執行隨機更新
            // 這樣可以避免旋轉螢幕時重新隨機
            if (savedInstanceState == null) {
                Log.d("HomeFragment", "Displaying random quote")
                updateRandomQuote()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::textQuote.isInitialized) {
            textQuote.textSize = DataManager.fontSize
        }
    }
}