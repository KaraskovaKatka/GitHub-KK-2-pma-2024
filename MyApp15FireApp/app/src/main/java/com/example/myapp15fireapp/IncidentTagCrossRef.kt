package com.example.myapp15fireapp

import androidx.room.Entity

@Entity(tableName = "incident_tag_cross_ref", primaryKeys = ["incidentId", "tagId"])

data class IncidentTagCrossRef(
    val incidentId: Int,  // ID výjezdu
    val tagId: Int        // ID štítku
)