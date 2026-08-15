package com.example.bigdocteur.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedecinDao {
    @Query("SELECT * FROM medecins")
    fun getAllMedecins(): Flow<List<Medecin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedecin(medecin: Medecin)

    @Delete
    suspend fun deleteMedecin(medecin: Medecin)

    @Query("SELECT COUNT(*) FROM medecins")
    fun getMedecinCount(): Flow<Int>

    @Query("SELECT * FROM medecins WHERE id = :id")
    fun getMedecinById(id: String): Flow<Medecin?>

    @Query("SELECT * FROM medecins WHERE nom LIKE :searchQuery OR specialite LIKE :searchQuery")
    fun searchMedecins(searchQuery: String): Flow<List<Medecin>>
}
