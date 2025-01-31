package com.example.myapp15fireapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp15fireapp.databinding.ItemNoteBinding
import kotlinx.coroutines.launch

class IncidentAdapter(
    private val lifecycleScope: LifecycleCoroutineScope,  // Přidán lifecycleScope
    private val database: IncidentHubDatabase,  // Přidána instance databáze
    private val incidents: List<Incident>,  // Seznam výjezdů místo poznámek
    private val onDeleteClick: (Incident) -> Unit,  // Funkce pro mazání výjezdu
    private val onEditClick: (Incident) -> Unit,    // Funkce pro editaci výjezdu
) : RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IncidentViewHolder(binding)
    }

    override fun getItemCount() = incidents.size

    override fun onBindViewHolder(holder: IncidentViewHolder, position: Int) {
        val incident = incidents[position]
        holder.bind(incident)
    }

    inner class IncidentViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(incident: Incident) {
            binding.noteTitle.text = incident.title
            binding.noteContentPreview.text = incident.content

            // Ověření, zda categoryId není null
            val categoryId = incident.categoryId
            if (categoryId != null) {
                lifecycleScope.launch {
                    val category = database.categoryDao().getCategoryById(categoryId)
                    if (category != null) {
                        binding.noteCategory.text = category.name  // Zobrazíme název kategorie
                    } else {
                        binding.noteCategory.text = "Neznámá kategorie"
                    }
                }
            } else {
                binding.noteCategory.text = "Bez kategorie"  // Pokud není přiřazena žádná kategorie
            }

            // Kliknutí na ikonu pro mazání
            binding.iconDelete.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle("Smazat výjezd")
                    .setMessage("Opravdu chcete toto hlášení z výjezdu smazat?")
                    .setPositiveButton("Ano") { _, _ ->
                        onDeleteClick(incident)  // Vyvolání funkce pro mazání výjezdu
                    }
                    .setNegativeButton("Ne", null)
                    .show()
            }

            // Kliknutí na ikonu pro editaci
            binding.iconEdit.setOnClickListener {
                onEditClick(incident)  // Vyvolání funkce pro editaci výjezdu
            }
        }
    }
}