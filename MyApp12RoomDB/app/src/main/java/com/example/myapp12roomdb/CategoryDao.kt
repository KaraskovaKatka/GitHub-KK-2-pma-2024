package com.example.myapp12roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow
import com.example.myapp12roomdb.CategoryDao

@Dao
interface CategoryDao {

    // Vloží novou kategorii do databáze
    @Insert
    suspend fun insert(category: Category)

    // Aktualizuje existující kategorii
    @Update
    suspend fun update(category: Category)

    // Smaže zadanou kategorii
    @Delete
    suspend fun delete(category: Category)

    // Načte všechny kategorie a vrátí je jako Flow
    @Query("SELECT * FROM category_table ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    // Najde kategorii podle jejího názvu
    @Query("SELECT * FROM category_table WHERE name = :name LIMIT 1")
    suspend fun getCategoryByName(name: String): Category?

    // Najde kategorii podle ID
    @Query("SELECT * FROM category_table WHERE id = :categoryId LIMIT 1")
    suspend fun getCategoryById(categoryId: Int): Category?

    // vymaže všechny možnosti kategorií z tabulky
    @Query("DELETE FROM category_table")
    suspend fun deleteAllCategories()

}