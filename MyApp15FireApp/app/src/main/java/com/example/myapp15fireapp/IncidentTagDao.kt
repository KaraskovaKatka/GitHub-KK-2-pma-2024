package com.example.myapp15fireapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentTagDao {

    // Vloží vazbu mezi výjezdem a štítkem
    @Insert
    suspend fun insert(incidentTagCrossRef: IncidentTagCrossRef)

    // Načte všechny štítky přidružené k určitému výjezdu
    @Transaction
    @Query("SELECT * FROM tag_table INNER JOIN incident_tag_cross_ref ON tag_table.id = incident_tag_cross_ref.tagId WHERE incident_tag_cross_ref.incidentId = :incidentId")
    fun getTagsForIncident(incidentId: Int): Flow<List<Tag>>

    // Načte všechny výjezdy přidružené k určitému štítku
    @Transaction
    @Query("SELECT * FROM incident_table INNER JOIN incident_tag_cross_ref ON incident_table.id = incident_tag_cross_ref.incidentId WHERE incident_tag_cross_ref.tagId = :tagId")
    fun getIncidentsForTag(tagId: Int): Flow<List<Incident>>
}