package com.example.myapp05moreactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapp05moreactivities.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Přijetí dat z MainActivity
        val receivedData = intent.getStringExtra("input_data")
        binding.tvReceivedData.text = "Přijatá data: $receivedData"

        // Tlačítko pro přechod na ThirdActivity
        binding.btnGoToThirdActivity.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("additional_data", "Data z druhé aktivity") // Nová data pro ThirdActivity
            startActivity(intent)
        }
    }
}