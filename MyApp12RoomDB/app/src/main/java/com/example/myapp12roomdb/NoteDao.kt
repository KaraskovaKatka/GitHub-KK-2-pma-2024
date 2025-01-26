package com.example.myapp12roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.example.myapp12roomdb.Note

@Dao //anotace- to co začíná zavináčem, jaké metody budu využívat nad tou danou tabulkou
interface NoteDao {

    // Vloží novou poznámku do databáze
    @Insert
    suspend fun insert(note: Note)

    // Aktualizuje existující poznámku
    @Update
    suspend fun update(note: Note)

    // Smaže zadanou poznámku
    @Delete
    suspend fun delete(note: Note)

    // Načte všechny poznámky a vrátí je jako Flow, které umožňuje pozorování změn
    @Query("SELECT * FROM note_table ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>// *znamená všechny SLOUPCE, note_table název tabulky, order by id- seřadit dle ID, DESC- sestupně seřadit

    // Vymaže všechny poznámky z tabulky
    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()

    //metodu pro filtrování podle kategorie
    @Query("SELECT * FROM note_table WHERE categoryId = :categoryId")
    fun getNotesByCategoryId(categoryId: Int): Flow<List<Note>>

}