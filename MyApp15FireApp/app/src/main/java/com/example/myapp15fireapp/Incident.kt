package com.example.myapp15fireapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incident_table")

data class Incident(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // ID poznámky, automaticky generované
    val title: String,  // Název poznámky
    val content: String,  // Obsah poznámky
    val categoryId: Int? = null,  // Volitelný odkaz na kategorii
    val date: String? = null,  // Volitelný datum
    val rating: Float = 0f,  // Volitelná hodnocení
    val location: String? = null  // Volitelná poloha
)
