package com.example

import android.app.Application
import com.example.data.local.AppDatabase
import com.example.data.repository.HistoryRepository
import com.example.data.repository.NotesRepository
import com.example.data.repository.SettingsRepository
import com.example.data.repository.TasksRepository

class AIDailyHelperApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val notesRepository: NotesRepository by lazy { NotesRepository(database.noteDao()) }
    val tasksRepository: TasksRepository by lazy { TasksRepository(database.taskDao()) }
    val historyRepository: HistoryRepository by lazy { HistoryRepository(database.historyDao()) }
    val settingsRepository: SettingsRepository by lazy { SettingsRepository(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: AIDailyHelperApplication
            private set
    }
}
