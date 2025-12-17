package com.example.quoteapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val imageRes: Int,      // 內建圖示 ID
    val imageUri: String? = null, // 自訂圖片路徑
    val userId: Int = -1 // 加入 User ID 以區分資料 (預設 -1 為訪客/系統)
)