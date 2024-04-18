package com.example.taskapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.taskapp.database.AppDatabase
import com.example.taskapp.databinding.ActivityMainBinding
import com.example.taskapp.fragments.CompletedTasksFragment
import com.example.taskapp.fragments.HomeFragment
import com.example.taskapp.repository.TaskRepository
import com.example.taskapp.viewmodel.TaskViewModel
import com.example.taskapp.viewmodel.TaskViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var taskViewModel: TaskViewModel
    lateinit var bottomNav: BottomNavigationView
    private lateinit var firebaseAuth: FirebaseAuth
    private  lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        firebaseAuth = FirebaseAuth.getInstance()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        setupViewModel()

        bottomNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.done ->{
                    loadFragment(CompletedTasksFragment())
                    true

                }
                R.id.logout -> {
                    firebaseAuth.signOut()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    false
                }
            }

        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(mainBinding.fragmentContainerView.id, fragment)
        transaction.commit()
    }

    // Construyendo nuestro viewModel para crear el evento de llamar nuestro modelo-vista
    private fun setupViewModel(){
        val taskRepository = TaskRepository(AppDatabase(this))
        val viewModelProviderFactory = TaskViewModelFactory(application, taskRepository)
        taskViewModel = ViewModelProvider(this, viewModelProviderFactory)[TaskViewModel::class.java]
    }
}