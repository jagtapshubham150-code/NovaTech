package com.example.data.repository

import com.example.data.local.dao.HistoryDao
import com.example.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val historyDao: HistoryDao) {
    val allHistory: Flow<List<HistoryEntity>> = historyDao.getAllHistory()

    fun getHistoryByType(type: String): Flow<List<HistoryEntity>> = historyDao.getHistoryByType(type)

    fun searchHistory(query: String): Flow<List<HistoryEntity>> = historyDao.searchHistory(query)

    suspend fun insertHistory(item: HistoryEntity): Long = historyDao.insertHistory(item)

    suspend fun deleteHistory(item: HistoryEntity) = historyDao.deleteHistory(item)

    suspend fun deleteHistoryById(id: Long) = historyDao.deleteHistoryById(id)

    suspend fun deleteAllHistory() = historyDao.deleteAllHistory()
}
