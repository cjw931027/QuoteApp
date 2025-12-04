package com.example.quoteapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.quoteapp.Category

@Dao
interface CategoryDao {
    // 取得所有分類
    @Query("SELECT * FROM categories ORDER BY id ASC")
    fun getAllCategories(): List<Category>

    // 新增分類
    @Insert
    fun insertCategory(category: Category)

    // [新增] 刪除分類
    @Delete
    fun deleteCategory(category: Category)

    // 檢查是否有同名分類
    @Query("SELECT COUNT(*) FROM categories WHERE name = :name")
    fun getCountByName(name: String): Int

    // 計算總數 (用來判斷是否需要初始化)
    @Query("SELECT COUNT(*) FROM categories")
    fun getCount(): Int
}