package com.example.quoteapp

import android.content.Intent
import android.os.Bundle
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

        fun updateQuote() {
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

        btnRefresh.setOnClickListener { updateQuote() }

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

        // [新增] 檢查是否是從通知點進來的
        val intent = requireActivity().intent
        val notifText = intent.getStringExtra("notification_quote_text")
        val notifAuthor = intent.getStringExtra("notification_quote_author")

        if (!notifText.isNullOrEmpty() && !notifAuthor.isNullOrEmpty()) {
            // 如果有通知傳來的資料，直接顯示該則名言
            textQuote.text = notifText
            textAuthor.text = "— $notifAuthor"

            // 建立一個臨時的 Quote 物件給 lastQuote (避免切換時重複)
            lastQuote = Quote(text = notifText, author = notifAuthor, category = "")

            // 移除資料，避免旋轉螢幕或下次進入時重複觸發
            intent.removeExtra("notification_quote_text")
            intent.removeExtra("notification_quote_author")
        } else {
            // 沒有通知資料，維持原本的隨機邏輯
            updateQuote()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::textQuote.isInitialized) {
            textQuote.textSize = DataManager.fontSize
        }
    }
}