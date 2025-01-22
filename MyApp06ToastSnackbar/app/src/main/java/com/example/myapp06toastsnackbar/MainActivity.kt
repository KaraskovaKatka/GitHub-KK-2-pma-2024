package com.example.myapp06toastsnackbar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp06toastsnackbar.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import android.view.Gravity
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tlačítko pro Toast
        binding.btnShowToast.setOnClickListener {
            showCustomToast("Toto je Toast s ikonou!")
        }

        // Tlačítko pro Snackbar
        binding.btnShowSnackbar.setOnClickListener {
            Snackbar.make(binding.root, "Toto je Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Zavřít") {
                    Toast.makeText(this, "Snackbar zavřen", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    // Funkce pro zobrazení vlastního Toastu
    private fun showCustomToast(message: String) {
        // Načtení vlastního layoutu pro Toast
        val inflater = layoutInflater
        val layout = inflater.inflate(
            R.layout.custom_toast,
            findViewById(R.id.custom_toast_container)
        )

        // Nastavení textu a ikony
        val text: TextView = layout.findViewById(R.id.toast_text)
        val image: ImageView = layout.findViewById(R.id.toast_icon)
        text.text = message
        image.setImageResource(R.drawable.toast_icon)

        // Vytvoření a zobrazení Toastu
        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.setGravity(Gravity.BOTTOM, 0, 200)
        toast.show()
    }
}