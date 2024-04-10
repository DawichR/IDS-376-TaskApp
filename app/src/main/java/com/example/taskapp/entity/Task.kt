package com.example.taskapp.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// Definir estructura como entidad y a que tabla se referencia
@Entity(tableName = "Task")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val Id: Int,
    val Title: String,
    val Description: String
): Parcelable

/*
* El atributo de parcelize es para hacer que este tipo de clases sea facil para transportar datos
* en los fragments y activities
* */