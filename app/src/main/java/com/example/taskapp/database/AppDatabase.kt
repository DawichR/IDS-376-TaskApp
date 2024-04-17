package com.example.taskapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.taskapp.entity.Task

@Database(entities = [Task::class], version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getTaskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Eliminar la base de datos existente si existe
                database.execSQL("DROP TABLE IF EXISTS `Task`")
                // Crear una nueva tabla con la estructura actualizada
                database.execSQL("""CREATE TABLE IF NOT EXISTS `Task` (
                            `Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            `Title` TEXT NOT NULL,
                            `Description` TEXT NOT NULL,
                            `IsCompleted` INTEGER NOT NULL DEFAULT 0,
                            `DueDate` TEXT NOT NULL,
                            `UserId` TEXT
                        );"""
                        )
            }
        }

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
            ).fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_1_2)
                .build()
    }
}