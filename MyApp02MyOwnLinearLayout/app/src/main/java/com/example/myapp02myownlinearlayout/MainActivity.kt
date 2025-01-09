package com.example.myapp02myownlinearlayout

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializace prvků
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val input1EditText = findViewById<EditText>(R.id.input1EditText)
        val input2EditText = findViewById<EditText>(R.id.input2EditText)
        val input3EditText = findViewById<EditText>(R.id.input3EditText)
        val input4EditText = findViewById<EditText>(R.id.input4EditText)
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        val outputEditText = findViewById<View>(R.id.outputEditText)

        // Nastavení akce pro tlačítko
        confirmButton.setOnClickListener {
            val input1 = input1EditText.text.toString()
            val input2 = input2EditText.text.toString()
            val input3 = input3EditText.text.toString()

            // Dynamická změna barvy výstupu podle vstupů
            val color = when {
                input1.equals("jméno hráče", true) -> Color.parseColor("#FFC0CB")
                input2.equals("tým", true) -> Color.parseColor("#D8BFD8")
                input3.equals("pozice", true) -> Color.parseColor("#ADD8E6")
                else -> Color.LTGRAY // Výchozí barva
            }

            outputEditText.setBackgroundColor(color)
        }
    }
}