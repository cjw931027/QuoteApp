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
    // 取得指定使用者的所有名言
    @Query("SELECT * FROM quotes WHERE userId = :userId ORDER BY id DESC")
    fun getAllQuotes(userId: Int): List<Quote>

    // 依照分類取得指定使用者的名言
    @Query("SELECT * FROM quotes WHERE category = :category AND userId = :userId ORDER BY id DESC")
    fun getQuotesByCategory(category: String, userId: Int): List<Quote>

    // 取得指定使用者的收藏名言
    @Query("SELECT * FROM quotes WHERE isFavorite = 1 AND userId = :userId ORDER BY id DESC")
    fun getFavoriteQuotes(userId: Int): List<Quote>

    // 新增名言
    @Insert
    fun insertQuote(quote: Quote)

    // 更新名言 (用來切換收藏狀態)
    @Update
    fun updateQuote(quote: Quote)

    // 刪除名言
    @Delete
    fun deleteQuote(quote: Quote)

    // 計算特定使用者的資料總筆數
    @Query("SELECT COUNT(*) FROM quotes WHERE userId = :userId")
    fun getCount(userId: Int): Int
}