package com.example.bigdocteur

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bigdocteur.data.Medecin
import java.io.File

class MedecinAdapter(private val onItemClick: (Medecin) -> Unit) : ListAdapter<Medecin, MedecinAdapter.MedecinViewHolder>(MedecinDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedecinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medecin, parent, false)
        return MedecinViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: MedecinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MedecinViewHolder(itemView: View, private val onItemClick: (Medecin) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvNom: TextView = itemView.findViewById(R.id.tvNom)
        private val tvSpecialite: TextView = itemView.findViewById(R.id.tvSpecialite)
        private val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhotoMedecin)

        fun bind(medecin: Medecin) {
            tvNom.text = medecin.nom
            tvSpecialite.text = medecin.specialite
            
            medecin.photoPath?.let { path ->
                try {
                    val file = File(path)
                    if (file.exists()) {
                        ivPhoto.setImageURI(Uri.fromFile(file))
                        ivPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                    } else {
                        ivPhoto.setImageResource(android.R.drawable.ic_menu_myplaces)
                    }
                } catch (e: Exception) {
                    ivPhoto.setImageResource(android.R.drawable.ic_menu_myplaces)
                }
            } ?: run {
                ivPhoto.setImageResource(android.R.drawable.ic_menu_myplaces)
            }
            
            itemView.setOnClickListener { onItemClick(medecin) }
        }
    }

    class MedecinDiffCallback : DiffUtil.ItemCallback<Medecin>() {
        override fun areItemsTheSame(oldItem: Medecin, newItem: Medecin): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Medecin, newItem: Medecin): Boolean {
            return oldItem == newItem
        }
    }
}
