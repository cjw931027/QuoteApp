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
        val btnShare = view.findViewById<View>(R.id.btn_share) // 取得分享按鈕

        fun updateQuote() {
            val allQuotes = DataManager.quotes
            if (allQuotes.isEmpty()) return // 避免沒資料時崩潰

            if (allQuotes.size == 1) {
                val q = allQuotes[0]
                textQuote.text = q.text
                textAuthor.text = "— ${q.author}"
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

        // 分享按鈕點擊事件
        btnShare.setOnClickListener {
            val quoteContent = textQuote.text.toString()
            val quoteAuthor = textAuthor.text.toString()

            // 組合分享文字
            val shareText = "$quoteContent\n$quoteAuthor\n\n- 來自 QuoteApp"

            // 建立分享 Intent
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            // 啟動分享選單
            val shareIntent = Intent.createChooser(sendIntent, "分享名言")
            startActivity(shareIntent)
        }

        updateQuote()
    }

    override fun onResume() {
        super.onResume()
        if (::textQuote.isInitialized) {
            textQuote.textSize = DataManager.fontSize
        }
    }
}