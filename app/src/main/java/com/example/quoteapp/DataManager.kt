package com.example.quoteapp

import android.content.Context
import com.example.quoteapp.data.QuoteDatabase

object DataManager {

    private lateinit var database: QuoteDatabase

    // --- 初始化 (必須在 MainActivity 呼叫) ---
    fun init(context: Context) {
        // 1. 載入設定
        loadSettings(context)

        // 2. 初始化資料庫
        database = QuoteDatabase.getDatabase(context)

        // 3. 如果資料庫是空的，寫入預設資料
        if (database.quoteDao().getCount() == 0) {
            val defaultQuotes = listOf(
                Quote(text = "The only way to do great work is to love what you do.", author = "Steve Jobs", category = "勵志"),
                Quote(text = "Your time is limited, don't waste it living someone else’s life.", author = "Steve Jobs", category = "勵志"),
                Quote(text = "Love isn’t something you find. Love is something that finds you.", author = "Loretta Young", category = "愛情"),
                Quote(text = "Being deeply loved by someone gives you strength.", author = "Lao Tzu", category = "愛情"),
                Quote(text = "Life is 10% what happens to us and 90% how we react to it.", author = "Charles R. Swindoll", category = "人生"),
                Quote(text = "In the middle of difficulty lies opportunity.", author = "Albert Einstein", category = "人生"),
                Quote(text = "Choose a job you love, and you will never have to work a day in your life.", author = "Confucius", category = "工作"),
                Quote(text = "Pleasure in the job puts perfection in the work.", author = "Aristotle", category = "工作"),
                Quote(text = "A real friend is one who walks in when the rest of the world walks out.", author = "Walter Winchell", category = "友誼")
            )
            // 逐筆寫入
            defaultQuotes.forEach { database.quoteDao().insertQuote(it) }
        }
    }

    // --- 所有分類 (這部分保持不變，因為分類通常是固定的) ---
    val categories = mutableListOf(
        Category("勵志", R.drawable.cat_motivation),
        Category("人生", R.drawable.cat_life),
        Category("愛情", R.drawable.cat_love),
        Category("工作", R.drawable.cat_work),
        Category("友誼", R.drawable.cat_friendship),
        Category("其他", R.drawable.cat_others),
    )

    // --- 從資料庫動態取得資料 ---

    // 取得所有名言
    val quotes: List<Quote>
        get() = database.quoteDao().getAllQuotes()

    // 取得所有收藏
    val favorites: List<Quote>
        get() = database.quoteDao().getFavoriteQuotes()

    // --- 設定變數 ---
    var fontSize: Float = 18f
    var isDarkMode: Boolean = false
    private const val PREFS_NAME = "QuoteAppPrefs"
    private const val KEY_FONT_SIZE = "key_font_size"
    private const val KEY_DARK_MODE = "key_dark_mode"

    fun loadSettings(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        fontSize = prefs.getFloat(KEY_FONT_SIZE, 18f)
        isDarkMode = prefs.getBoolean(KEY_DARK_MODE, false)
    }

    fun saveSettings(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putFloat(KEY_FONT_SIZE, fontSize)
            putBoolean(KEY_DARK_MODE, isDarkMode)
            apply()
        }
    }

    // --- 功能操作 ---

    fun addCategory(name: String, iconRes: Int, imageUri: String? = null) {
        if (categories.none { it.name == name }) {
            categories.add(Category(name, iconRes, imageUri))
        }
    }

    // 新增名言到資料庫
    fun addQuote(text: String, author: String, category: String) {
        val newQuote = Quote(text = text, author = author, category = category)
        database.quoteDao().insertQuote(newQuote)
    }

    // 切換收藏狀態 (更新資料庫)
    fun toggleFavorite(quote: Quote) {
        // 反轉狀態
        quote.isFavorite = !quote.isFavorite
        // 更新資料庫
        database.quoteDao().updateQuote(quote)
    }
}