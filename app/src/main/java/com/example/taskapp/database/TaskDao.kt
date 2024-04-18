package com.example.taskapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskapp.entity.Task

@Dao
interface TaskDao {

    /*
    * OnConflict se encargara de reemplazar conflictos con respecto a primary keys que
    *  ya existan
    * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM TASK WHERE IsCompleted = 0 AND UserId = :userId ORDER BY Id DESC")
    fun  getAllTask(userId: String?): LiveData<List<Task>>

    @Query("SELECT * FROM TASK WHERE IsCompleted = 1 AND UserId = :userId ORDER BY Id DESC")
    fun  getAllCompletedTask(userId: String?): LiveData<List<Task>>


    @Query("SELECT * FROM TASK WHERE UserId = :userId AND (IsCompleted = 0 OR IsCompleted IS NULL) AND (Title LIKE :query OR Description like :query)")
    fun searchTask(query: String?, userId: String?): LiveData<List<Task>>

}