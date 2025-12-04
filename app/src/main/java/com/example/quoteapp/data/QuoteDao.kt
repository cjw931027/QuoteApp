package com.example.quoteapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quoteapp.Quote

@Dao
interface QuoteDao {
    // 取得所有名言 (依照 ID 倒序排列，新的在上面)
    @Query("SELECT * FROM quotes ORDER BY id DESC")
    fun getAllQuotes(): List<Quote>

    // 依照分類取得名言
    @Query("SELECT * FROM quotes WHERE category = :category ORDER BY id DESC")
    fun getQuotesByCategory(category: String): List<Quote>

    // 取得所有收藏的名言
    @Query("SELECT * FROM quotes WHERE isFavorite = 1 ORDER BY id DESC")
    fun getFavoriteQuotes(): List<Quote>

    // 新增名言
    @Insert
    fun insertQuote(quote: Quote)

    // 更新名言 (用來切換收藏狀態)
    @Update
    fun updateQuote(quote: Quote)

    // 刪除名言
    @Delete
    fun deleteQuote(quote: Quote)

    // 計算資料總筆數
    @Query("SELECT COUNT(*) FROM quotes")
    fun getCount(): Int
}