package com.example.myapp14christmasapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("AdventCalendarPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val calendarLayout = findViewById<GridLayout>(R.id.calendar)
        val resetButton = findViewById<Button>(R.id.reset_button)

        val themeSwitch = findViewById<Switch>(R.id.theme_switch)

        // Načtení předchozího stavu (pokud byl nastaven)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        themeSwitch.isChecked = isDarkMode

        // Nastavení počáteční barvy
        calendarLayout.setBackgroundColor(if (isDarkMode) Color.BLACK else Color.WHITE)

        // Přepínač reaguje na změnu režimu
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                calendarLayout.setBackgroundColor(Color.BLACK)  // Tmavý režim
                editor.putBoolean("dark_mode", true)
            } else {
                calendarLayout.setBackgroundColor(Color.WHITE)  // Světlý režim
                editor.putBoolean("dark_mode", false)
            }
            editor.apply()
        }

        val shareButton = findViewById<Button>(R.id.share_button)

        // Počet snězených políček se vypočítá podle SharedPreferences
        shareButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("AdventCalendarPrefs", Context.MODE_PRIVATE)
            var eatenCount = 0

            // Projdeme všechny dny a spočítáme snědené
            for (day in 1..24) {
                if (sharedPreferences.getString("day_$day", "closed") == "eaten") {
                    eatenCount++
                }
            }

            val remainingDays = 24 - eatenCount

            // Vytvoření sdílecího Intentu
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Snědl jsem už $eatenCount políček v mém adventním kalendáři! Zbývá mi ještě $remainingDays dní do Vánoc. 🎄✨")
            }
            startActivity(Intent.createChooser(intent, "Sdílet přes:"))
        }

        fun countEatenDays(sharedPreferences: SharedPreferences): Int {
            var count = 0
            for (day in 1..24) {
                if (sharedPreferences.getString("day_$day", "closed") == "eaten") {
                    count++
                }
            }
            return count
        }

        // Funkce na vytvoření kalendáře
        fun createCalendar() {
            calendarLayout.removeAllViews() // Vyčištění kalendáře
            for (day in 1..24) {
                val dayView = FrameLayout(this).apply {
                    setBackgroundColor(Color.parseColor("#006400")) 
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 200
                        height = 200
                        setMargins(25, 8, 8, 8)
                    }
                }

                val dayTextView = TextView(this).apply {
                    text = day.toString()
                    setTextColor(Color.WHITE)
                    textSize = 18f
                    gravity = Gravity.CENTER
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                }

                val state = sharedPreferences.getString("day_$day", "closed")
                when (state) {
                    "open" -> {
                        dayTextView.text = "" // Číslo zmizí
                    }
                    "eaten" -> {
                        dayTextView.text = "-" // Zobrazení pomlčky místo čísla
                    }
                }

                dayView.setOnClickListener {
                    val currentState = sharedPreferences.getString("day_$day", "closed")
                    val remainingDays = 24 - day // Počet dní do Vánoc

                    when (currentState) {
                        "closed" -> {
                            Snackbar.make(calendarLayout, "Otevřel jsi $day. políčko.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Sním to!") {
                                    Toast.makeText(this@MainActivity, "Snědl jsi $day. den, zbývá ještě $remainingDays dní do Vánoc.", Toast.LENGTH_SHORT).show()

                                    editor.putString("day_$day", "eaten").apply()

                                    // APLIKACE ANIMACE NA ZMĚNU TEXTU
                                    dayTextView.animate().alpha(0f).setDuration(500).withEndAction {
                                        // Kontrola, zda už bylo snědeno všech 24 políček
                                        val eatenCount = countEatenDays(sharedPreferences)

                                        if (eatenCount == 24) {
                                            dayTextView.text = "🎁 Veselé Vánoce!" // Po snědení všech políček se zobrazí text
                                        } else {
                                            dayTextView.text = "-" // Jinak normální pomlčka
                                        }

                                        dayTextView.animate().alpha(1f).setDuration(500).start()
                                    }.start()
                                }.show()
                        }
                        "open" -> {
                            Snackbar.make(calendarLayout, "Bereš si sladkost z $day. dne.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Sníst!") {
                                    Toast.makeText(this@MainActivity, "Snědl jsi $day. den, zbývá ještě $remainingDays dní do Vánoc.", Toast.LENGTH_SHORT).show()

                                    editor.putString("day_$day", "eaten").apply()

                                    // APLIKACE ANIMACE NA ZMĚNU TEXTU
                                    dayTextView.animate().alpha(0f).setDuration(500).withEndAction {
                                        val eatenCount = countEatenDays(sharedPreferences)

                                        if (eatenCount == 24) {
                                            dayTextView.text = "🎁 Veselé Vánoce!"
                                        } else {
                                            dayTextView.text = "-"
                                        }

                                        dayTextView.animate().alpha(1f).setDuration(500).start()
                                    }.start()
                                }.show()
                        }
                    }
                }

                dayView.addView(dayTextView)
                calendarLayout.addView(dayView)
            }
        }

        // Inicializace kalendáře při spuštění
        createCalendar()

        // Funkce tlačítka reset
        resetButton.setOnClickListener {
            editor.clear().apply() // Vymazání dat v SharedPreferences
            createCalendar() // Vytvoření nového kalendáře
            Toast.makeText(this, "Kalendář byl resetován!", Toast.LENGTH_SHORT).show()
        }
    }
}