package com.example.taskapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.entity.Task
import com.example.taskapp.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(app: Application, private val taskRepository: TaskRepository) : AndroidViewModel(app) {

    fun addTask(task: Task) =
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }

    fun updateTask(task: Task) =
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    fun deleteTask(task: Task) =
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }

    fun getAllTask() = taskRepository.getAllTask()

    fun searcNotes(query: String?) = taskRepository.searchTask(query)

}