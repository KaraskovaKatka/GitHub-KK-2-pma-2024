package com.example.myapp15fireapp

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object IncidentHubDatabaseInstance {

    @Volatile
    private var INSTANCE: IncidentHubDatabase? = null

    fun getDatabase(context: Context): IncidentHubDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                IncidentHubDatabase::class.java,
                "incidenthub_database"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5) // Přidána nová migrace
                .build()
            INSTANCE = instance
            instance
        }
    }

    // MIGRACE 1 → 2 (přidání sloupce date)
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE incident ADD COLUMN date TEXT")
        }
    }

    // MIGRACE 2 → 3 (přidání sloupce rating)
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE incident ADD COLUMN rating REAL NOT NULL DEFAULT 0.0")
        }
    }

    // MIGRACE 3 → 4 (pokud jste přidali nový sloupec, např. location)
    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE incident ADD COLUMN location TEXT DEFAULT NULL")
        }
    }

    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE incident ADD COLUMN address TEXT DEFAULT NULL")
        }
    }

}