package com.example.myapp08sharedpreferences

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp08sharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Název SharedPreferences
    private val PREFS_NAME = "MyAppPreferences"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Uložení hodnoty do SharedPreferences
        binding.btnSave.setOnClickListener {
            val inputText = binding.etInput.text.toString()
            if (inputText.isNotEmpty()) {
                saveToPreferences(inputText)
            }
        }

        // Načtení hodnoty ze SharedPreferences
        binding.btnLoad.setOnClickListener {
            val savedText = loadFromPreferences()
            binding.tvOutput.text = savedText
        }
    }

    // Funkce pro uložení hodnoty do SharedPreferences
    private fun saveToPreferences(text: String) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("savedText", text)
        editor.apply() // Uložení dat
        binding.tvOutput.text = "Text byl úspěšně uložen."
    }

    // Funkce pro načtení hodnoty ze SharedPreferences
    private fun loadFromPreferences(): String {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("savedText", "Žádná hodnota není uložena.") ?: "Žádná hodnota není uložena."
    }
}