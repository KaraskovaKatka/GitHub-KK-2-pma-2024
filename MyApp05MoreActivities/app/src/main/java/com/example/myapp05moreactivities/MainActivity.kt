package com.example.myapp05moreactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapp05moreactivities.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nastavení tlačítka pro odeslání dat
        binding.btnSend.setOnClickListener {
            val inputText = binding.etInput.text.toString() // Získání textu z EditText
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("input_data", inputText) // Odeslání dat do SecondActivity
            startActivity(intent)
        }
    }
}