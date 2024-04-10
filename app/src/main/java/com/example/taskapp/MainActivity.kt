package com.example.taskapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskapp.database.AppDatabase
import com.example.taskapp.repository.TaskRepository
import com.example.taskapp.viewmodel.TaskViewModel
import com.example.taskapp.viewmodel.TaskViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var taskViewModel: TaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
    }

    // Construyendo nuestro viewModel para crear el evento de llamar nuestro modelo-vista
    private fun setupViewModel(){
        val taskRepository = TaskRepository(AppDatabase(this))
        val viewModelProviderFactory = TaskViewModelFactory(application, taskRepository)
        taskViewModel = ViewModelProvider(this, viewModelProviderFactory)[TaskViewModel::class.java]
    }
}