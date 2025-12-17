package com.example.quoteapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.quoteapp.Category

@Dao
interface CategoryDao {
    // 取得指定使用者的所有分類
    @Query("SELECT * FROM categories WHERE userId = :userId ORDER BY id ASC")
    fun getAllCategories(userId: Int): List<Category>

    // 新增分類
    @Insert
    fun insertCategory(category: Category)

    // [新增] 刪除分類
    @Delete
    fun deleteCategory(category: Category)

    // 檢查是否有同名分類
    @Query("SELECT COUNT(*) FROM categories WHERE name = :name AND userId = :userId")
    fun getCountByName(name: String, userId: Int): Int

    // 計算特定使用者的總數 (用來判斷是否需要初始化)
    @Query("SELECT COUNT(*) FROM categories WHERE userId = :userId")
    fun getCount(userId: Int): Int
}