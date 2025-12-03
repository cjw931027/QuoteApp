package com.example.quoteapp

import android.content.Context

object DataManager {

    // --- 所有分類 ---
    val categories = mutableListOf(
        Category("勵志", R.drawable.cat_motivation),
        Category("人生", R.drawable.cat_life),
        Category("愛情", R.drawable.cat_love),
        Category("工作", R.drawable.cat_work),
        Category("友誼", R.drawable.cat_friendship),
        Category("其他", R.drawable.cat_others),
    )

    // --- 所有名言 ---
    val quotes = mutableListOf(
        Quote("The only way to do great work is to love what you do.", "Steve Jobs", "勵志"),
        Quote("Your time is limited, don't waste it living someone else’s life.", "Steve Jobs", "勵志"),
        Quote("Love isn’t something you find. Love is something that finds you.", "Loretta Young", "愛情"),
        Quote("Being deeply loved by someone gives you strength.", "Lao Tzu", "愛情"),
        Quote("Life is 10% what happens to us and 90% how we react to it.", "Charles R. Swindoll", "人生"),
        Quote("In the middle of difficulty lies opportunity.", "Albert Einstein", "人生"),
        Quote("Choose a job you love, and you will never have to work a day in your life.", "Confucius", "工作"),
        Quote("Pleasure in the job puts perfection in the work.", "Aristotle", "工作"),
        Quote("A real friend is one who walks in when the rest of the world walks out.", "Walter Winchell", "友誼"),
    )

    // --- 收藏 ---
    val favorites = mutableListOf<Quote>()

    // --- 設定變數 ---
    var fontSize: Float = 18f // 預設字體大小
    var isDarkMode: Boolean = false // 預設淺色模式

    // --- SharedPreferences 儲存與讀取 ---
    private const val PREFS_NAME = "QuoteAppPrefs"
    private const val KEY_FONT_SIZE = "key_font_size"
    private const val KEY_DARK_MODE = "key_dark_mode"

    // 初始化設定 (在 MainActivity 呼叫)
    fun loadSettings(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        fontSize = prefs.getFloat(KEY_FONT_SIZE, 18f)
        isDarkMode = prefs.getBoolean(KEY_DARK_MODE, false)
    }

    // 儲存設定 (在 SettingsFragment 呼叫)
    fun saveSettings(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putFloat(KEY_FONT_SIZE, fontSize)
            putBoolean(KEY_DARK_MODE, isDarkMode)
            apply()
        }
    }

    // --- 新增分類功能 ---
    fun addCategory(name: String, iconRes: Int, imageUri: String? = null) {
        if (categories.none { it.name == name }) {
            categories.add(Category(name, iconRes, imageUri))
        }
    }

    // --- 新增名言 ---
    fun addQuote(text: String, author: String, category: String) {
        quotes.add(Quote(text, author, category))
    }

    // --- 收藏名言 ---
    fun toggleFavorite(quote: Quote) {
        if (favorites.contains(quote)) {
            favorites.remove(quote)
        } else {
            favorites.add(quote)
        }
    }
}