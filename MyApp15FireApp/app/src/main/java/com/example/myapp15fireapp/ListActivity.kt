package com.example.myapp15fireapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity) // Nahraďte odpovídajícím layoutem

        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
    }
}