package com.example.myapp15fireapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {

    // Vloží nový výjezd do databáze
    @Insert  suspend fun insert(incident: Incident)

    // Aktualizuje existující výjezd
    @Update  suspend fun update(incident: Incident)

    // Smaže zadaný výjezd
    @Delete  suspend fun delete(incident: Incident)

    // Načte všechny výjezdy a vrátí je jako Flow, které umožňuje pozorování změn
    @Query("SELECT * FROM incident_table ORDER BY id DESC")
    fun getAllIncidents(): Flow<List<Incident>>

    // Vymaže všechny záznamy z tabulky
    @Query("DELETE FROM incident_table")
    suspend fun deleteAllIncidents()

    // Načte výjezdy podle kategorie
    @Query("SELECT * FROM incident_table WHERE categoryId = :categoryId")
    fun getIncidentsByCategoryId(categoryId: Int): Flow<List<Incident>>
}