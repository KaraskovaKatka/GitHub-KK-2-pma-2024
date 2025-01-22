package com.example.myapp09datastore

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp09datastore.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

// Vytvoření DataStore
private val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Klíč pro uložení textu
    private val TEXT_KEY = stringPreferencesKey("savedText")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Uložení hodnoty do DataStore
        binding.btnSave.setOnClickListener {
            val inputText = binding.etInput.text.toString()
            if (inputText.isNotEmpty()) {
                saveToDataStore(inputText)
            }
        }

        // Načtení hodnoty z DataStore
        binding.btnLoad.setOnClickListener {
            loadFromDataStore()
        }
    }

    // Uložení textu do DataStore
    private fun saveToDataStore(text: String) {
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences[TEXT_KEY] = text
            }
            binding.tvOutput.text = "Text byl úspěšně uložen."
        }
    }

    // Načtení textu z DataStore
    private fun loadFromDataStore() {
        lifecycleScope.launch {
            val preferences = dataStore.data.first()
            val savedText = preferences[TEXT_KEY] ?: "Žádná hodnota není uložena."
            binding.tvOutput.text = savedText
        }
    }
}