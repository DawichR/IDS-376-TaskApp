package com.example.taskapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskapp.entity.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getTaskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?:
            createDatabase(context).also{
                instance = it
                println("Instancia de base de datos creada!")
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "task_db"
            ).build()
    }
}