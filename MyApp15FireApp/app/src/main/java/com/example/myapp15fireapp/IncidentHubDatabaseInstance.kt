package com.example.myapp15fireapp

import android.content.Context
import androidx.room.Room

object IncidentHubDatabaseInstance {

    // Lazy inicializace instance databáze
    @Volatile
    private var INSTANCE: IncidentHubDatabase? = null

    // Funkce pro získání instance databáze
    fun getDatabase(context: Context): IncidentHubDatabase {
        // Pokud je instance null, inicializuje ji
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                IncidentHubDatabase::class.java,
                "incidenthub_database"  // Změna názvu souboru databáze
            ).build()
            INSTANCE = instance
            instance
        }
    }
}