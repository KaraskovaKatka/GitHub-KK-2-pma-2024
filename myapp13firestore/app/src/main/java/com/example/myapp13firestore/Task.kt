package com.example.myapp13firestore

data class Task(
    val id: String = "" ,// Identifikátor úkolu (pro Firestore)
    var name: String = "" ,// Název úkolu
    var isCompleted: Boolean = false, // Stav dokončení
    var assignedTo: String = "" // Kdo si úkol vzal
)