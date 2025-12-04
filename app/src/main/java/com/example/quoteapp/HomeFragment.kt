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

        // [關鍵修改] 檢查是否從通知進入
        val intent = requireActivity().intent
        val isFromNotification = intent.getBooleanExtra("from_notification", false)

        Log.d("HomeFragment", "Is from notification: $isFromNotification")

        if (isFromNotification) {
            // 從 DataManager (Prefs) 讀取通知的那句名言
            val notifQuote = DataManager.getNotificationQuote(requireContext())

            if (notifQuote != null) {
                Log.d("HomeFragment", "Displaying stored notification quote")
                textQuote.text = notifQuote.text
                textAuthor.text = "— ${notifQuote.author}"
                lastQuote = notifQuote

                // 移除 Flag，避免旋轉螢幕時重複觸發 (雖然邏輯上沒差，但為了保險)
                intent.removeExtra("from_notification")
            } else {
                // 如果讀不到，就隨機
                updateRandomQuote()
            }
        } else {
            // 一般進入：只有第一次載入時隨機 (避免旋轉螢幕重抽)
            if (savedInstanceState == null) {
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