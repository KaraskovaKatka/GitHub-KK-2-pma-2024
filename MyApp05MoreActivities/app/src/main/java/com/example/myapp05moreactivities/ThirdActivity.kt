package com.example.myapp05moreactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapp05moreactivities.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Přijetí dat z SecondActivity
        val receivedData = intent.getStringExtra("additional_data")
        binding.tvFinalData.text = "Přijatá data: $receivedData"
    }
}