package com.example.myapp15fireapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Incident::class, Category::class, Tag::class, IncidentTagCrossRef::class],  // Změna entit na Incident
    version = 1,
    exportSchema = false
)
abstract class IncidentHubDatabase : RoomDatabase() {

    abstract fun incidentDao(): IncidentDao  // Změna metody na incidentDao
    abstract fun categoryDao(): CategoryDao
    abstract fun tagDao(): TagDao
    abstract fun incidentTagDao(): IncidentTagDao  // Změna metody na incidentTagDao
}