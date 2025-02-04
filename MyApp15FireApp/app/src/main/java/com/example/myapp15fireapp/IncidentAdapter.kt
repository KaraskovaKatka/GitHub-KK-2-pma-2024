package com.example.myapp15fireapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp15fireapp.databinding.ItemNoteBinding
import kotlinx.coroutines.launch

class IncidentAdapter(
    private val lifecycleScope: LifecycleCoroutineScope,
    private val database: IncidentHubDatabase,
    private val incidents: List<Incident>,
    private val onDeleteClick: (Incident) -> Unit,
    private val onEditClick: (Incident) -> Unit,

) : RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder>() {

    // Vytvoření ViewHolderu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IncidentViewHolder(binding)
    }

    // Nastavení počtu položek v seznamu
    override fun getItemCount(): Int = incidents.size

    // Připojení dat k ViewHolderu
    override fun onBindViewHolder(holder: IncidentViewHolder, position: Int) {
        val incident = incidents[position]
        holder.bind(incident)
    }

    // Vnitřní třída ViewHolder
    inner class IncidentViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(incident: Incident) {
            binding.noteTitle.text = incident.title
            binding.noteContentPreview.text = incident.content
            binding.noteAddress.text = incident.location ?: "Adresa neuložena"
            binding.noteDate.text = incident.date ?: "Datum neuvedeno"
            binding.noteRatingBar.rating = incident.rating ?: 0f // OPRAVENO - místo `!!`

            // Ověření kategorie
            val categoryId = incident.categoryId
            if (categoryId != null) {
                lifecycleScope.launch {
                    val category = database.categoryDao().getCategoryById(categoryId)
                    binding.noteCategory.text = category?.name ?: "Neznámá kategorie"
                }
            } else {
                binding.noteCategory.text = "Bez kategorie"
            }

            // Nastavení kliknutí na mazání
            binding.iconDelete.setOnClickListener {
                onDeleteClick(incident)
            }

            // Nastavení kliknutí na editaci
            binding.iconEdit.setOnClickListener {
                onEditClick(incident)
            }
        }
    }
}