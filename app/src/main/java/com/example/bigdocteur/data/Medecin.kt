package com.example.bigdocteur.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medecins")
data class Medecin(
    @PrimaryKey val id: String,
    val nom: String,
    val specialite: String,
    val dateNaissance: String,
    val photoPath: String? = null
)
