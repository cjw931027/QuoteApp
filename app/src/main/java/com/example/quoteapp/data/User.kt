package com.example.quoteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val password: String, // In a real app, this should be hashed
    val username: String,
    val avatarResId: Int = 0, // 使用者頭貼資源 ID (預設 0 代表無或預設)
    val avatarUri: String? = null // 自訂頭貼 URI (字串格式)
)
