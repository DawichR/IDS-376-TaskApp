package com.example.taskapp.repository

import com.example.taskapp.database.AppDatabase
import com.example.taskapp.entity.Task

class TaskRepository(private val db: AppDatabase) {
    suspend fun insertTask(task: Task) = db.getTaskDao().insertTask(task)
    suspend fun updateTask(task: Task) = db.getTaskDao().updateTask(task)
    suspend fun deleteTask(task: Task) = db.getTaskDao().deleteTask(task)
    fun getAllTask() = db.getTaskDao().getAllTask()
    fun searchTask(query: String?) = db.getTaskDao().searchTask(query)


}