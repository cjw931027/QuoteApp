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

        // 3. 如果資料庫是空的 (沒有名言)，寫入預設名言
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
            defaultQuotes.forEach { database.quoteDao().insertQuote(it) }
        }

        // 4. 如果資料庫是空的 (沒有分類)，寫入預設分類
        if (database.categoryDao().getCount() == 0) {
            val defaultCategories = listOf(
                Category(name = "勵志", imageRes = R.drawable.cat_motivation),
                Category(name = "人生", imageRes = R.drawable.cat_life),
                Category(name = "愛情", imageRes = R.drawable.cat_love),
                Category(name = "工作", imageRes = R.drawable.cat_work),
                Category(name = "友誼", imageRes = R.drawable.cat_friendship),
                Category(name = "其他", imageRes = R.drawable.cat_others)
            )
            defaultCategories.forEach { database.categoryDao().insertCategory(it) }
        }
    }

    // --- 從資料庫動態取得資料 ---

    // 取得所有分類
    val categories: List<Category>
        get() = database.categoryDao().getAllCategories()

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

    // 新增分類到資料庫
    fun addCategory(name: String, iconRes: Int, imageUri: String? = null) {
        if (database.categoryDao().getCountByName(name) == 0) {
            val newCategory = Category(name = name, imageRes = iconRes, imageUri = imageUri)
            database.categoryDao().insertCategory(newCategory)
        }
    }

    // [新增] 刪除分類 (以及該分類下的所有名言)
    fun deleteCategory(category: Category) {
        // 1. 先刪除該分類下的所有名言 (因為名言是用 category 字串做關聯)
        val quotesToDelete = database.quoteDao().getQuotesByCategory(category.name)
        quotesToDelete.forEach {
            database.quoteDao().deleteQuote(it)
        }

        // 2. 再刪除分類本身
        database.categoryDao().deleteCategory(category)
    }

    // 新增名言到資料庫
    fun addQuote(text: String, author: String, category: String) {
        val newQuote = Quote(text = text, author = author, category = category)
        database.quoteDao().insertQuote(newQuote)
    }

    // [新增] 刪除名言
    fun deleteQuote(quote: Quote) {
        database.quoteDao().deleteQuote(quote)
    }

    // 切換收藏狀態 (更新資料庫)
    fun toggleFavorite(quote: Quote) {
        quote.isFavorite = !quote.isFavorite
        database.quoteDao().updateQuote(quote)
    }
}