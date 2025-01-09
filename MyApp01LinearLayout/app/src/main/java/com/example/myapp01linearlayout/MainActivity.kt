package com.example.myapp01linearlayout

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstNameEditText = findViewById<EditText>(R.id.firstNameEditText)
        val lastNameEditText = findViewById<EditText>(R.id.lastNameEditText)
        val cityEditText = findViewById<EditText>(R.id.cityEditText)
        val ageEditText = findViewById<EditText>(R.id.ageEditText)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val clearButton = findViewById<Button>(R.id.clearButton)
        val textView = findViewById<TextView>(R.id.textView)

        submitButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val city = cityEditText.text.toString()
            val age = ageEditText.text.toString()

            textView.text = "Jméno: $firstName $lastName\nObec: $city\nVěk: $age"
        }

        clearButton.setOnClickListener {
            firstNameEditText.text.clear()
            lastNameEditText.text.clear()
            cityEditText.text.clear()
            ageEditText.text.clear()
            textView.text = ""
        }
    }
}