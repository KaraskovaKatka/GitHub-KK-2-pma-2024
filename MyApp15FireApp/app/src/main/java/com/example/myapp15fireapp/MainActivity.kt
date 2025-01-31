package com.example.myapp15fireapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp15fireapp.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var isNameAscending = true
    private var currentCategory: String = "Vše"
    private lateinit var binding: ActivityMainBinding
    private lateinit var incidentAdapter: IncidentAdapter
    private lateinit var database: IncidentHubDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Moje výjezdy"

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializace databáze
        database = IncidentHubDatabaseInstance.getDatabase(this)
        insertDefaultCategories()
        insertDefaultTags()
        setupUI()

        // Inicializace RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.fabAddNote.setOnClickListener {
            showAddIncidentDialog()
        }

        // Načtení výjezdů z databáze
        loadIncidents()
    }

    private fun showAddIncidentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val contentEditText = dialogView.findViewById<EditText>(R.id.editTextContent)
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinnerCategory)

        lifecycleScope.launch {
            val categories = database.categoryDao().getAllCategories().first()
            val categoryNames = categories.map { it.name }
            val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Přidat výjezd")
            .setView(dialogView)
            .setPositiveButton("Přidat") { _, _ ->
                val title = titleEditText.text.toString()
                val content = contentEditText.text.toString()
                val selectedCategory = spinnerCategory.selectedItem.toString()

                lifecycleScope.launch {
                    val category = database.categoryDao().getCategoryByName(selectedCategory)
                    if (category != null) {
                        addIncidentToDatabase(title, content, category.id)
                    }
                }
            }
            .setNegativeButton("Zrušit", null)
            .create()

        dialog.show()
    }

    private fun addIncidentToDatabase(title: String, content: String, categoryId: Int) {
        lifecycleScope.launch {
            val newIncident = Incident(title = title, content = content, categoryId = categoryId)
            database.incidentDao().insert(newIncident)
            loadIncidents()
        }
    }

    private fun loadIncidents() {
        lifecycleScope.launch {
            var incidents = if (currentCategory == "Vše") {
                database.incidentDao().getAllIncidents().first()
            } else {
                val category = database.categoryDao().getCategoryByName(currentCategory)
                if (category != null) {
                    database.incidentDao().getIncidentsByCategoryId(category.id).first()
                } else {
                    emptyList()
                }
            }

            if (isNameAscending) {
                incidents = incidents.sortedWith(compareBy { it.title?.lowercase() ?: "" })
            } else {
                incidents = incidents.sortedWith(compareByDescending { it.title?.lowercase() ?: "" })
            }

            incidentAdapter = IncidentAdapter(
                incidents = incidents,
                onDeleteClick = { incident -> deleteIncident(incident) },
                onEditClick = { incident -> editIncident(incident) },
                lifecycleScope = lifecycleScope,
                database = database
            )
            binding.recyclerView.adapter = incidentAdapter
        }
    }

    private fun insertSampleIncidents() {
        lifecycleScope.launch {
            val sampleIncidents = listOf(
                Incident(title = "Výjezd 1", content = "Obsah první testovací poznámky"),
                Incident(title = "Výjezd 2", content = "Obsah druhé testovací poznámky"),
                Incident(title = "Výjezd 3", content = "Obsah třetí testovací poznámky")
            )
            sampleIncidents.forEach { database.incidentDao().insert(it) }
        }
    }

    private fun insertDefaultCategories() {
        lifecycleScope.launch {
            database.categoryDao().deleteAllCategories()
            val defaultCategories = listOf(
                "Požár",
                "Dopravní nehoda",
                "Technická pomoc",
                "Záchrana osob a zvířat",
                "Únik nebezpečných látek",
                "Jiné")
            for (categoryName in defaultCategories) {
                val existingCategory = database.categoryDao().getCategoryByName(categoryName)
                if (existingCategory == null) {
                    database.categoryDao().insert(Category(name = categoryName))
                }
            }
        }
    }

    private fun insertDefaultTags() {
        lifecycleScope.launch {
            val existingTags = database.tagDao().getAllTags().first()
            if (existingTags.isEmpty()) {
                val defaultTags = listOf(
                    Tag(name = "Požár"),
                    Tag(name = "Požár - popelnice, kontejner"),
                    Tag(name = "Požár - odpad, ostatní"),
                    Tag(name = "Požár - nízké budovy"),
                    Tag(name = "Požár - průmyslové, zemědělské objekty,sklady"),
                    Tag(name = "Požár - saze v komíně"),
                    Tag(name = "Dopravní nehoda - úklid vozovky"),
                    Tag(name = "Dopravní nehoda - uvolnění komunikace, odtažení"),
                    Tag(name = "Dopravní nehoda - železniční"),
                    Tag(name = "Dopravní nehoda - vyproštění osob"),
                    Tag(name = "Dopravní nehoda - se zraněním"),
                    Tag(name = "Technická pomoc - odstranění stromu"),
                    Tag(name = "Technická pomoc - otevření uzavřených prostor"),
                    Tag(name = "Technická pomoc - transport pacienta"),
                    Tag(name = "Záchrana osob a zvířat - AED") ,
                    Tag(name = "Záchrana osob a zvířat - uzavřené prostory") ,
                    Tag(name = "Záchrana osob a zvířat - ostatní") ,
                    Tag(name = "Planý poplach"),
                    Tag(name = "Únik nebezpečných látek - do ovzduší"),
                    Tag(name = "Únik nebezpečných látek - na pozemní komunikaci"),
                    Tag(name = "Jiné - zatím neurčeno ")
                )
                defaultTags.forEach { database.tagDao().insert(it) }
            }
        }
    }

    private fun deleteIncident(incident: Incident) {
        lifecycleScope.launch {
            database.incidentDao().delete(incident)
            loadIncidents()
        }
    }

    private fun editIncident(incident: Incident) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val contentEditText = dialogView.findViewById<EditText>(R.id.editTextContent)
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinnerCategory)

        lifecycleScope.launch {
            val categories = database.categoryDao().getAllCategories().first()
            val categoryNames = categories.map { it.name }
            val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter

            val noteCategoryId = incident.categoryId
            if (noteCategoryId != null) {
                val currentCategory = categories.firstOrNull { it.id == noteCategoryId }
                val categoryIndex = currentCategory?.let { categoryNames.indexOf(it.name) } ?: 0
                spinnerCategory.setSelection(categoryIndex)
            }
        }

        titleEditText.setText(incident.title)
        contentEditText.setText(incident.content)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Upravit výjezd")
            .setView(dialogView)
            .setPositiveButton("Uložit") { _, _ ->
                val updatedTitle = titleEditText.text.toString()
                val updatedContent = contentEditText.text.toString()
                val selectedCategory = spinnerCategory.selectedItem.toString()

                lifecycleScope.launch {
                    val category = database.categoryDao().getCategoryByName(selectedCategory)
                    if (category != null) {
                        val updatedIncident = incident.copy(
                            title = updatedTitle,
                            content = updatedContent,
                            categoryId = category.id
                        )
                        database.incidentDao().update(updatedIncident)
                        loadIncidents()
                    }
                }
            }
            .setNegativeButton("Zrušit", null)
            .create()

        dialog.show()
    }

    private fun setupUI() {
        setupFilterSpinner()
        setupSortButtons()
    }

    private fun setupFilterSpinner() {
        lifecycleScope.launch {
            val categories = database.categoryDao().getAllCategories().first()
            val categoryNames = categories.map { it.name }.toMutableList()
            categoryNames.add(0, "Vše")

            val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerFilterCategory.adapter = adapter

            binding.spinnerFilterCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    currentCategory = categoryNames[position]
                    loadIncidents()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun setupSortButtons() {
        binding.btnSortByName.setOnClickListener {
            isNameAscending = !isNameAscending
            loadIncidents()
        }
    }
}