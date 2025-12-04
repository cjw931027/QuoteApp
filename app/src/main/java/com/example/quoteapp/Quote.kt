package com.example.quoteapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // 自動產生 ID
    val text: String,
    val author: String,
    val category: String,
    var isFavorite: Boolean = false // 直接在資料庫記錄是否收藏
)