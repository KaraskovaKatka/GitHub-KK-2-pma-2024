package com.example.myapp12roomdb

import android.app.AlertDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp12roomdb.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var database: NoteHubDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // inicializace RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
       //  noteAdapter = NoteAdapter(getSampleNotes()) dát do komentáře
        binding.recyclerView.adapter = noteAdapter


        // noteAdapter = NoteAdapter(emptyList()) to samé - Inicializace s prázdným seznamem
        // binding.recyclerView.adapter = noteAdapter

        // inicializace databáze
        database = NoteHubDatabaseInstance.getDatabase(this)

             // Vložení výchozích kategorií a štítků do databáze
             insertDefaultCategories()
             insertDefaultTags()

        // insertSampleNotes() - Vložení testovacích dat komentář

        // Načtení poznámek z databáze
        loadNotes()

        binding.fabAddNote.setOnClickListener {
            showAddNoteDialog()
        }

    }

    private fun showAddNoteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val contentEditText = dialogView.findViewById<EditText>(R.id.editTextContent)
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinnerCategory)

        // Načtení kategorií z databáze a jejich zobrazení ve Spinneru
        lifecycleScope.launch {
            val categories = database.categoryDao().getAllCategories().first()  // Načteme kategorie
            val categoryNames = categories.map { it.name }  // Převedeme na seznam názvů kategorií
            val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Přidat poznámku")
            .setView(dialogView)
            .setPositiveButton("Přidat") { _, _ ->
                val title = titleEditText.text.toString()
                val content = contentEditText.text.toString()
                val selectedCategory = spinnerCategory.selectedItem.toString()  // Získáme vybranou kategorii

                // Najdeme ID vybrané kategorie
                lifecycleScope.launch {
                    val category = database.categoryDao().getCategoryByName(selectedCategory)
                    if (category != null) {
                        addNoteToDatabase(title, content, category.id)
                    }
                }
            }
            .setNegativeButton("Zrušit", null)
            .create()

        dialog.show()
    }

    private fun addNoteToDatabase(title: String, content: String, categoryId: Int) {
        lifecycleScope.launch {
            val newNote = Note(title = title, content = content, categoryId = categoryId)
            database.noteDao().insert(newNote)  // Vloží poznámku do databáze
            loadNotes()  // Aktualizuje seznam poznámek
        }
    }

    private fun showAddNoteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val contentEditText = dialogView.findViewById<EditText>(R.id.editTextContent)
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinnerCategory)


        val dialog = AlertDialog.Builder(this)
            .setTitle("Přidat poznámku")
            .setView(dialogView)
            .setPositiveButton("Přidat") { _, _ ->
                val title = titleEditText.text.toString()
                val content = contentEditText.text.toString()
                addNoteToDatabase(title, content)
            }
            .setNegativeButton("Zrušit", null)
            .create()

        dialog.show()
    }

    private fun loadNotes() {
        lifecycleScope.launch {
            database.noteDao().getAllNotes().collect { notes ->
                noteAdapter = NoteAdapter(notes,
                    onDeleteClick = {note -> deleteNote(note)},
                    onEditClick = {note -> editNote(note)}
                            lifecycleScope = lifecycleScope,  // Předáváme lifecycleScope
                    database = database  // Předáváme databázi
                           )
                binding.recyclerView.adapter = noteAdapter
            }
        }
    }

    private fun insertSampleNotes() {
        lifecycleScope.launch {
            val sampleNotes = listOf(
                Note(title = "Vzorek 1", content = "Obsah první testovací poznámky"),
                Note(title = "Vzorek 2", content = "Obsah druhé testovací poznámky"),
                Note(title = "Vzorek 3", content = "Obsah třetí testovací poznámky")
            )
            sampleNotes.forEach { database.noteDao().insert(it) }
        }
    }

    private fun insertDefaultCategories() {
        lifecycleScope.launch {
            val defaultCategories = listOf(
                "Osobní",
                "Práce",
                "Nápady"
            )

            for (categoryName in defaultCategories) {
                val existingCategory = database.categoryDao().getCategoryByName(categoryName)
                if (existingCategory == null) {
                    // Kategorie s tímto názvem neexistuje, vložíme ji
                    database.categoryDao().insert(Category(name = categoryName))
                }
            }
        }


        private fun insertDefaultTags() {
        lifecycleScope.launch {
            val existingTags = database.tagDao().getAllTags().first()  // Použití first() pro získání seznamu
            if (existingTags.isEmpty()) {
                val defaultTags = listOf(
                    Tag(name = "Důležité"),
                    Tag(name = "Rychlé úkoly"),
                    Tag(name = "Projekt"),
                    Tag(name = "Nápad")
                )
                defaultTags.forEach { database.tagDao().insert(it) }
            }
        }
    }

    private fun deleteNote(note: Note) {
        lifecycleScope.launch {
            database.noteDao().delete(note)  // Smazání poznámky z databáze
            loadNotes()  // Aktualizace seznamu poznámek
        }
    }

    private fun editNote(note: Note) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val contentEditText = dialogView.findViewById<EditText>(R.id.editTextContent)

        // Předvyplnění stávajících dat poznámky
        titleEditText.setText(note.title)
        contentEditText.setText(note.content)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Upravit poznámku")
            .setView(dialogView)
            .setPositiveButton("Uložit") { _, _ ->
                val updatedTitle = titleEditText.text.toString()
                val updatedContent = contentEditText.text.toString()

                // Aktualizace poznámky v databázi
                lifecycleScope.launch {
                    val updatedNote = note.copy(title = updatedTitle, content = updatedContent)
                    database.noteDao().update(updatedNote)  // Uloží aktualizovanou poznámku
                    loadNotes()  // Načte a aktualizuje seznam poznámek
                }
            }
            .setNegativeButton("Zrušit", null)
            .create()

        dialog.show()
    }

    private fun getSampleNotes(): List<Note> {
        // Testovací seznam poznámek
        return listOf(
            Note(title = "Poznámka 1", content = "Obsah první poznámky"),
            Note(title = "Poznámka 2", content = "Obsah druhé poznámky"),
            Note(title = "Poznámka 3", content = "Obsah třetí poznámky")
        )
    }

    }
