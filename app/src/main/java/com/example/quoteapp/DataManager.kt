package com.example.quoteapp

import android.content.Context
import com.example.quoteapp.data.QuoteDatabase

object DataManager {

    private lateinit var database: QuoteDatabase

    fun init(context: Context) {
        loadSettings(context)
        database = QuoteDatabase.getDatabase(context)

        // 檢查若資料庫是空的，則寫入更多預設名言 (中英文混合)
        if (database.quoteDao().getCount() == 0) {
            val defaultQuotes = listOf(
                // --- 勵志 (Motivation) ---
                Quote(text = "The only way to do great work is to love what you do.", author = "Steve Jobs", category = "勵志"),
                Quote(text = "偉大的工作唯一的方法就是熱愛你所做的事。", author = "史蒂夫·賈伯斯", category = "勵志"),
                Quote(text = "Believe you can and you're halfway there.", author = "Theodore Roosevelt", category = "勵志"),
                Quote(text = "相信自己，你已經成功了一半。", author = "西奧多·羅斯福", category = "勵志"),
                Quote(text = "It does not matter how slowly you go as long as you do not stop.", author = "Confucius", category = "勵志"),
                Quote(text = "不論走得多慢，只要不停止，就一定能到達。", author = "孔子", category = "勵志"),
                Quote(text = "Failure is the mother of success.", author = "Proverb", category = "勵志"),
                Quote(text = "失敗為成功之母。", author = "諺語", category = "勵志"),
                Quote(text = "Your time is limited, don't waste it living someone else’s life.", author = "Steve Jobs", category = "勵志"),

                // --- 人生 (Life) ---
                Quote(text = "Life is what happens when you're busy making other plans.", author = "John Lennon", category = "人生"),
                Quote(text = "人生就是當你忙著制定其他計畫時，所發生的事。", author = "約翰·藍儂", category = "人生"),
                Quote(text = "Get busy living or get busy dying.", author = "Stephen King", category = "人生"),
                Quote(text = "忙著活，或是忙著死。", author = "史蒂芬·金", category = "人生"),
                Quote(text = "Life is 10% what happens to us and 90% how we react to it.", author = "Charles R. Swindoll", category = "人生"),
                Quote(text = "In the middle of difficulty lies opportunity.", author = "Albert Einstein", category = "人生"),
                Quote(text = "The purpose of our lives is to be happy.", author = "Dalai Lama", category = "人生"),
                Quote(text = "人生的目的在於追求快樂。", author = "達賴喇嘛", category = "人生"),

                // --- 愛情 (Love) ---
                Quote(text = "The best thing to hold onto in life is each other.", author = "Audrey Hepburn", category = "愛情"),
                Quote(text = "生命中最好的依靠是彼此。", author = "奧黛麗·赫本", category = "愛情"),
                Quote(text = "To love and be loved is to feel the sun from both sides.", author = "David Viscott", category = "愛情"),
                Quote(text = "愛與被愛，就像從兩面感受陽光。", author = "大衛·維斯考特", category = "愛情"),
                Quote(text = "Love isn’t something you find. Love is something that finds you.", author = "Loretta Young", category = "愛情"),
                Quote(text = "Being deeply loved by someone gives you strength.", author = "Lao Tzu", category = "愛情"),

                // --- 工作 (Work) ---
                Quote(text = "Choose a job you love, and you will never have to work a day in your life.", author = "Confucius", category = "工作"),
                Quote(text = "選擇一份你熱愛的工作，你這輩子就再也不用工作了。", author = "孔子", category = "工作"),
                Quote(text = "Success is not the key to happiness. Happiness is the key to success.", author = "Albert Schweitzer", category = "工作"),
                Quote(text = "成功不是快樂的關鍵，快樂才是成功的關鍵。", author = "阿爾伯特·史懷哲", category = "工作"),
                Quote(text = "Pleasure in the job puts perfection in the work.", author = "Aristotle", category = "工作"),
                Quote(text = "樂在工作，才能成就完美。", author = "亞里斯多德", category = "工作"),
                Quote(text = "Genius is one percent inspiration and ninety-nine percent perspiration.", author = "Thomas Edison", category = "工作"),

                // --- 友誼 (Friendship) ---
                Quote(text = "A friend is someone who knows all about you and still loves you.", author = "Elbert Hubbard", category = "友誼"),
                Quote(text = "朋友是那個知道你的一切，卻依然愛你的人。", author = "埃爾伯特·哈伯德", category = "友誼"),
                Quote(text = "Walking with a friend in the dark is better than walking alone in the light.", author = "Helen Keller", category = "友誼"),
                Quote(text = "與朋友在黑暗中同行，勝過獨自在光明中行走。", author = "海倫·凱勒", category = "友誼"),
                Quote(text = "A real friend is one who walks in when the rest of the world walks out.", author = "Walter Winchell", category = "友誼")
            )
            defaultQuotes.forEach { database.quoteDao().insertQuote(it) }
        }

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

    val categories: List<Category>
        get() = database.categoryDao().getAllCategories()

    val quotes: List<Quote>
        get() = database.quoteDao().getAllQuotes()

    val favorites: List<Quote>
        get() = database.quoteDao().getFavoriteQuotes()

    var fontSize: Float = 18f
    var isDarkMode: Boolean = false
    var isNotificationEnabled: Boolean = true
    var notificationHour: Int = 9
    var notificationMinute: Int = 0

    private const val PREFS_NAME = "QuoteAppPrefs"
    private const val KEY_FONT_SIZE = "key_font_size"
    private const val KEY_DARK_MODE = "key_dark_mode"
    private const val KEY_NOTIF_ENABLED = "key_notif_enabled"
    private const val KEY_NOTIF_HOUR = "key_notif_hour"
    private const val KEY_NOTIF_MINUTE = "key_notif_minute"

    // [新增] 用來儲存通知名言的 Key
    private const val KEY_NOTIF_QUOTE_TEXT = "key_notif_text"
    private const val KEY_NOTIF_QUOTE_AUTHOR = "key_notif_author"

    fun loadSettings(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        fontSize = prefs.getFloat(KEY_FONT_SIZE, 18f)
        isDarkMode = prefs.getBoolean(KEY_DARK_MODE, false)
        isNotificationEnabled = prefs.getBoolean(KEY_NOTIF_ENABLED, true)
        notificationHour = prefs.getInt(KEY_NOTIF_HOUR, 9)
        notificationMinute = prefs.getInt(KEY_NOTIF_MINUTE, 0)
    }

    fun saveSettings(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putFloat(KEY_FONT_SIZE, fontSize)
            putBoolean(KEY_DARK_MODE, isDarkMode)
            putBoolean(KEY_NOTIF_ENABLED, isNotificationEnabled)
            putInt(KEY_NOTIF_HOUR, notificationHour)
            putInt(KEY_NOTIF_MINUTE, notificationMinute)
            apply()
        }
    }

    // [新增] 儲存通知選到的名言
    fun saveNotificationQuote(context: Context, quote: Quote) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_NOTIF_QUOTE_TEXT, quote.text)
            putString(KEY_NOTIF_QUOTE_AUTHOR, quote.author)
            apply()
        }
    }

    // [新增] 讀取通知名言
    fun getNotificationQuote(context: Context): Quote? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val text = prefs.getString(KEY_NOTIF_QUOTE_TEXT, null)
        val author = prefs.getString(KEY_NOTIF_QUOTE_AUTHOR, null)
        return if (text != null && author != null) {
            Quote(text = text, author = author, category = "Notification")
        } else null
    }

    fun addCategory(name: String, iconRes: Int, imageUri: String? = null) {
        if (database.categoryDao().getCountByName(name) == 0) {
            val newCategory = Category(name = name, imageRes = iconRes, imageUri = imageUri)
            database.categoryDao().insertCategory(newCategory)
        }
    }

    fun deleteCategory(category: Category) {
        val quotesToDelete = database.quoteDao().getQuotesByCategory(category.name)
        quotesToDelete.forEach { database.quoteDao().deleteQuote(it) }
        database.categoryDao().deleteCategory(category)
    }

    fun addQuote(text: String, author: String, category: String) {
        val newQuote = Quote(text = text, author = author, category = category)
        database.quoteDao().insertQuote(newQuote)
    }

    fun deleteQuote(quote: Quote) {
        database.quoteDao().deleteQuote(quote)
    }

    fun toggleFavorite(quote: Quote) {
        quote.isFavorite = !quote.isFavorite
        database.quoteDao().updateQuote(quote)
    }
}