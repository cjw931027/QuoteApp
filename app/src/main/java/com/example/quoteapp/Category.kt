package com.example.quoteapp

data class Category(
    val name: String,
    val imageRes: Int,
    val imageUri: String? = null // 新增：如果是自訂圖片，這裡會有值
)