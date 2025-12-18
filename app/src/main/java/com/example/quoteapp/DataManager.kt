package com.example.quoteapp

import android.content.Context
import com.example.quoteapp.data.QuoteDatabase
import com.example.quoteapp.data.User

object DataManager {

    private lateinit var database: QuoteDatabase
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
        loadSettings(context)
        database = QuoteDatabase.getDatabase(context)

        // 初始化訪客 (UserID = -1) 的資料
        initializeUserContent(-1)
    }

    // [新增] 初始化指定使用者的預設資料 (若該使用者資料是空的)
    private fun initializeUserContent(userId: Int) {
        if (database.quoteDao().getCount(userId) == 0) {
            val defaultQuotes = getDefaultQuotes(userId)
            defaultQuotes.forEach { database.quoteDao().insertQuote(it) }
        }

        if (database.categoryDao().getCount(userId) == 0) {
            val defaultCategories = getDefaultCategories(userId)
            defaultCategories.forEach { database.categoryDao().insertCategory(it) }
        }
    }

    private fun getDefaultQuotes(userId: Int): List<Quote> = listOf(
                // --- 勵志 (Motivation) ---
                Quote(text = "The only way to do great work is to love what you do.", author = "Steve Jobs", category = "勵志", userId = userId),
                Quote(text = "偉大的工作唯一的方法就是熱愛你所做的事。", author = "史蒂夫·賈伯斯", category = "勵志", userId = userId),
                Quote(text = "Believe you can and you're halfway there.", author = "Theodore Roosevelt", category = "勵志", userId = userId),
                Quote(text = "相信自己，你已經成功了一半。", author = "西奧多·羅斯福", category = "勵志", userId = userId),
                Quote(text = "It does not matter how slowly you go as long as you do not stop.", author = "Confucius", category = "勵志", userId = userId),
                Quote(text = "不論走得多慢，只要不停止，就一定能到達。", author = "孔子", category = "勵志", userId = userId),
                Quote(text = "Failure is the mother of success.", author = "Proverb", category = "勵志", userId = userId),
                Quote(text = "失敗為成功之母。", author = "諺語", category = "勵志", userId = userId),
                Quote(text = "Your time is limited, don't waste it living someone else’s life.", author = "Steve Jobs", category = "勵志", userId = userId),

                // --- 人生 (Life) ---
                Quote(text = "Life is what happens when you're busy making other plans.", author = "John Lennon", category = "人生", userId = userId),
                Quote(text = "人生就是當你忙著制定其他計畫時，所發生的事。", author = "約翰·藍儂", category = "人生", userId = userId),
                Quote(text = "Get busy living or get busy dying.", author = "Stephen King", category = "人生", userId = userId),
                Quote(text = "忙著活，或是忙著死。", author = "史蒂芬·金", category = "人生", userId = userId),
                Quote(text = "Life is 10% what happens to us and 90% how we react to it.", author = "Charles R. Swindoll", category = "人生", userId = userId),
                Quote(text = "In the middle of difficulty lies opportunity.", author = "Albert Einstein", category = "人生", userId = userId),
                Quote(text = "The purpose of our lives is to be happy.", author = "Dalai Lama", category = "人生", userId = userId),
                Quote(text = "人生的目的在於追求快樂。", author = "達賴喇嘛", category = "人生", userId = userId),

                // --- 愛情 (Love) ---
                Quote(text = "The best thing to hold onto in life is each other.", author = "Audrey Hepburn", category = "愛情", userId = userId),
                Quote(text = "生命中最好的依靠是彼此。", author = "奧黛麗·赫本", category = "愛情", userId = userId),
                Quote(text = "To love and be loved is to feel the sun from both sides.", author = "David Viscott", category = "愛情", userId = userId),
                Quote(text = "愛與被愛，就像從兩面感受陽光。", author = "大衛·維斯考特", category = "愛情", userId = userId),
                Quote(text = "Love isn’t something you find. Love is something that finds you.", author = "Loretta Young", category = "愛情", userId = userId),
                Quote(text = "Being deeply loved by someone gives you strength.", author = "Lao Tzu", category = "愛情", userId = userId),

                // --- 工作 (Work) ---
                Quote(text = "Choose a job you love, and you will never have to work a day in your life.", author = "Confucius", category = "工作", userId = userId),
                Quote(text = "選擇一份你熱愛的工作，你這輩子就再也不用工作了。", author = "孔子", category = "工作", userId = userId),
                Quote(text = "Success is not the key to happiness. Happiness is the key to success.", author = "Albert Schweitzer", category = "工作", userId = userId),
                Quote(text = "成功不是快樂的關鍵，快樂才是成功的關鍵。", author = "阿爾伯特·史懷哲", category = "工作", userId = userId),
                Quote(text = "Pleasure in the job puts perfection in the work.", author = "Aristotle", category = "工作", userId = userId),
                Quote(text = "樂在工作，才能成就完美。", author = "亞里斯多德", category = "工作", userId = userId),
                Quote(text = "Genius is one percent inspiration and ninety-nine percent perspiration.", author = "Thomas Edison", category = "工作", userId = userId),

                // --- 友誼 (Friendship) ---
                Quote(text = "A friend is someone who knows all about you and still loves you.", author = "Elbert Hubbard", category = "友誼", userId = userId),
                Quote(text = "朋友是那個知道你的一切，卻依然愛你的人。", author = "埃爾伯特·哈伯德", category = "友誼", userId = userId),
                Quote(text = "Walking with a friend in the dark is better than walking alone in the light.", author = "Helen Keller", category = "友誼", userId = userId),
                Quote(text = "與朋友在黑暗中同行，勝過獨自在光明中行走。", author = "海倫·凱勒", category = "友誼", userId = userId),
                Quote(text = "A real friend is one who walks in when the rest of the world walks out.", author = "Walter Winchell", category = "友誼", userId = userId)
            )
    
    private fun getDefaultCategories(userId: Int): List<Category> = listOf(
        Category(name = "勵志", imageRes = R.drawable.cat_motivation, userId = userId),
        Category(name = "人生", imageRes = R.drawable.cat_life, userId = userId),
        Category(name = "愛情", imageRes = R.drawable.cat_love, userId = userId),
        Category(name = "工作", imageRes = R.drawable.cat_work, userId = userId),
        Category(name = "友誼", imageRes = R.drawable.cat_friendship, userId = userId),
        Category(name = "其他", imageRes = R.drawable.cat_others, userId = userId)
    )

    val categories: List<Category>
        get() = database.categoryDao().getAllCategories(currentUserId)

    val quotes: List<Quote>
        get() = database.quoteDao().getAllQuotes(currentUserId)

    val favorites: List<Quote>
        get() = database.quoteDao().getFavoriteQuotes(currentUserId)

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

    private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
    private const val KEY_USER_ID = "key_user_id"

    fun loadSettings(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        fontSize = prefs.getFloat(KEY_FONT_SIZE, 18f)
        isDarkMode = prefs.getBoolean(KEY_DARK_MODE, false)
        isNotificationEnabled = prefs.getBoolean(KEY_NOTIF_ENABLED, true)
        notificationHour = prefs.getInt(KEY_NOTIF_HOUR, 9)

        notificationMinute = prefs.getInt(KEY_NOTIF_MINUTE, 0)
        
        isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        currentUserId = prefs.getInt(KEY_USER_ID, -1)
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
        if (database.categoryDao().getCountByName(name, currentUserId) == 0) {
            val newCategory = Category(name = name, imageRes = iconRes, imageUri = imageUri, userId = currentUserId)
            database.categoryDao().insertCategory(newCategory)
        }
    }

    fun deleteCategory(category: Category) {
        val quotesToDelete = database.quoteDao().getQuotesByCategory(category.name, currentUserId)
        quotesToDelete.forEach { database.quoteDao().deleteQuote(it) }
        database.categoryDao().deleteCategory(category)
    }

    fun addQuote(text: String, author: String, category: String) {
        val newQuote = Quote(text = text, author = author, category = category, userId = currentUserId)
        database.quoteDao().insertQuote(newQuote)
    }

    fun deleteQuote(quote: Quote) {
        database.quoteDao().deleteQuote(quote)
    }

    fun toggleFavorite(quote: Quote) {
        quote.isFavorite = !quote.isFavorite
        database.quoteDao().updateQuote(quote)
    }

    // --- User Management ---
    var isLoggedIn: Boolean = false
        private set
    var currentUserId: Int = -1
        private set

    fun login(username: String, password: String): User? {
        val user = database.userDao().getUserByUsername(username)
        if (user != null && user.password == password) {
            isLoggedIn = true
            currentUserId = user.id
            saveUserSession()
            return user
        }
        return null
    }

    // Google Login Mock
    fun loginWithGoogle(): Boolean {
        // Mock successful login
        isLoggedIn = true
        currentUserId = -1 // Special ID for guest/google mock if we don't save them to DB, or create a mock user
        // Let's create a mock user for Google if not exists or just set session
        saveUserSession()
        return true
    }

    fun register(username: String, email: String, password: String, avatarResId: Int, avatarUri: String? = null): Boolean {
        // Check if username already exists
        val existingUser = database.userDao().getUserByUsername(username)
        if (existingUser != null) return false // Username taken

        val normalizedEmail = email.lowercase()
        // Optional: still check email if we want unique emails too, but user asked for username login.
        // Let's keep email check to avoid confusion/duplicates, but primary is username now for login.
        val existingEmail = database.userDao().getUserByEmail(normalizedEmail)
        if (existingEmail != null) return false

        val newUser = User(username = username, email = normalizedEmail, password = password, avatarResId = avatarResId, avatarUri = avatarUri)
        val id = database.userDao().insertUser(newUser)
        if (id > 0) {
            // [關鍵] 註冊成功後，為新使用者初始化預設名言和分類
            initializeUserContent(id.toInt())
            login(username, password) // Auto login after register
            return true
        }
        return false
    }

    fun logout() {
        isLoggedIn = false
        currentUserId = -1
        saveUserSession()
    }

    fun getCurrentUser(): User? {
        if (!isLoggedIn || currentUserId == -1) return null
        return database.userDao().getUserById(currentUserId)
    }

    fun updateUserAvatar(uri: String) {
        val user = getCurrentUser()
        if (user != null) {
            val updatedUser = user.copy(avatarUri = uri)
            database.userDao().updateUser(updatedUser)
        }
    }

    private fun saveUserSession() {
        if (!::appContext.isInitialized) return
        val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            putInt(KEY_USER_ID, currentUserId)
            apply()
        }
    }
}