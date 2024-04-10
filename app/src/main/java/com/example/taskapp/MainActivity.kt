package com.example.taskapp

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskapp.database.AppDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val context = applicationContext
        val statusTextView = findViewById<TextView>(R.id.statusTextView)
        try {
            val database = AppDatabase(context)
            database.getTaskDao().getAllTask()
            statusTextView.text = "La instancia de la base de datos esta creada"
        } catch (e: Exception) {
            statusTextView.text = "Ha ocurrido un error en la base de daots: ${e.message}"
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}