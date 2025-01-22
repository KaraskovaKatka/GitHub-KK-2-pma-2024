package com.example.myapp03ordersystem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp03ordersystem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Inicializace View Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Aktivace View Binding
        setContentView(binding.root)

        setupListeners() // Nastavení posluchačů pro interakce
    }

    private fun setupListeners() {
        // Nastavení změny obrázků podle výběru RadioButton
        binding.radioGroupKaktus.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.kaktus_1 -> binding.imageBike.setImageResource(R.drawable.kaktus1)
                R.id.kaktus_2 -> binding.imageBike.setImageResource(R.drawable.kaktus2)
                R.id.kaktus_3 -> binding.imageBike.setImageResource(R.drawable.kaktus3)
            }
        }

        // Logika pro tlačítko Objednat
        binding.btnOrder.setOnClickListener {
            val selectedKaktus = when (binding.radioGroupKaktus.checkedRadioButtonId) {
                R.id.kaktus_1 -> "Kaktus 1"
                R.id.kaktus_2 -> "Kaktus 2"
                R.id.kaktus_3 -> "Kaktus 3"
                else -> "Žádný kaktus"
            }

            val options = mutableListOf<String>()
            if (binding.checkboxBetterFork.isChecked) options.add("Víc pichlavý")
            if (binding.checkboxBetterSaddle.isChecked) options.add("Hezčí vzhled do kanceláře")
            if (binding.checkboxCarbonHandlebar.isChecked) options.add("Moderní vzhled")

            val summary = "Kaktus: $selectedKaktus\nDoplňky: ${if (options.isEmpty()) "Žádné" else options.joinToString(", ")}"
            binding.tvSummary.text = summary
        }
    }
}