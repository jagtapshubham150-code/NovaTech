package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val toolType: String,
    val title: String,
    val inputText: String,
    val outputText: String,
    val timestamp: Long = System.currentTimeMillis()
)
