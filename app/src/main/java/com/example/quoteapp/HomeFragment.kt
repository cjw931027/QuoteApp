package com.example.quoteapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var lastQuote: Quote? = null  //  記住上一句名言

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textQuote = view.findViewById<TextView>(R.id.text_quote)
        val textAuthor = view.findViewById<TextView>(R.id.text_author)
        val btnRefresh = view.findViewById<Button>(R.id.btn_refresh)

        fun updateQuote() {

            val allQuotes = DataManager.quotes

            // 只有一句名言就直接顯示（避免 infinite loop）
            if (allQuotes.size == 1) {
                val q = allQuotes[0]
                textQuote.text = q.text
                textAuthor.text = "— ${q.author}"
                return
            }

            var newQuote: Quote

            //  持續抽，直到不是上一句
            do {
                newQuote = allQuotes.random()
            } while (newQuote == lastQuote)

            // 更新 UI
            textQuote.text = newQuote.text
            textAuthor.text = "— ${newQuote.author}"

            lastQuote = newQuote //  記住這次抽到的
        }

        btnRefresh.setOnClickListener { updateQuote() }

        updateQuote() // App 啟動先抽一次
    }
}
