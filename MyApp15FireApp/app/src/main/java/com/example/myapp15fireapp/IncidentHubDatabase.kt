package com.example.myapp15fireapp

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Incident::class, Category::class, Tag::class, IncidentTagCrossRef::class],  // Změna entit na Incident
    version = 5,
    exportSchema = false
)

abstract class IncidentHubDatabase : RoomDatabase() {
    abstract fun incidentDao(): IncidentDao  // Změna metody na incidentDao
    abstract fun categoryDao(): CategoryDao
    abstract fun tagDao(): TagDao
    abstract fun incidentTagDao(): IncidentTagDao  // Změna metody na incidentTagDao

    companion object {
        val Migration_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE incident ADD COLUMN address TEXT DEFAULT NULL")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE incident_table ADD COLUMN rating REAL DEFAULT 0.0")
            }
        }

        val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                // Přidání sloupce `rating` s výchozí hodnotou 0
                database.execSQL("ALTER TABLE incident ADD COLUMN rating REAL NOT NULL DEFAULT 0.0")
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) { // Definice migrace
            override fun migrate(database: SupportSQLiteDatabase) {
                // Přidání nového sloupce `date` do tabulky `incident`
                database.execSQL("ALTER TABLE incident ADD COLUMN date TEXT")
            }
        }
    }
}