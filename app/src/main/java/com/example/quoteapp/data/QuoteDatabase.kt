package com.example.quoteapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quoteapp.Category
import com.example.quoteapp.Quote

// 1. 在 entities 中加入 Category::class
// 2. 將 version 版本號升級為 6 (因為更動了引用這的資料結構)
@Database(entities = [Quote::class, Category::class, User::class], version = 6, exportSchema = false)
abstract class QuoteDatabase : RoomDatabase() {

    abstract fun quoteDao(): QuoteDao

    // 新增：提供 CategoryDao 的存取方法
    abstract fun categoryDao(): CategoryDao

    // 新增：提供 UserDao 的存取方法
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: QuoteDatabase? = null

        fun getDatabase(context: Context): QuoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java,
                    "quote_database"
                )
                    // 為了開發方便，允許在主執行緒查詢
                    .allowMainThreadQueries()
                    // 新增：如果資料庫版本不符 (例如從 v1 升級到 v2)，直接重建資料庫 (會清除舊資料)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}