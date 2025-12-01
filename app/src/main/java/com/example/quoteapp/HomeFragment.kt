package com.example.quoteapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var lastQuote: Quote? = null
    private lateinit var textQuote: TextView // 提升為類別成員變數

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textQuote = view.findViewById(R.id.text_quote) // 初始化
        val textAuthor = view.findViewById<TextView>(R.id.text_author)
        val btnRefresh = view.findViewById<Button>(R.id.btn_refresh)

        fun updateQuote() {
            val allQuotes = DataManager.quotes
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
        updateQuote()
    }

    // 每次回到頁面時，套用字體大小設定
    override fun onResume() {
        super.onResume()
        if (::textQuote.isInitialized) {
            textQuote.textSize = DataManager.fontSize
        }
    }
}