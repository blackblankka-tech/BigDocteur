package com.example.bigdocteur

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.bigdocteur.data.AppDatabase
import com.example.bigdocteur.data.Medecin
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.io.File

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val header = findViewById<View>(R.id.header)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            bottomNavigation.setPadding(0, 0, 0, systemBars.bottom)
            header.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        val database = AppDatabase.getDatabase(this)
        val medecinId = intent.getStringExtra("MEDECIN_ID")

        if (medecinId == null) {
            Toast.makeText(this, "Erreur : ID manquant", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val imgPhoto = findViewById<ImageView>(R.id.imageMedecin)
        val tvNom = findViewById<TextView>(R.id.nomMedecin)
        val tvSpecialite = findViewById<TextView>(R.id.specialiteMedecin)
        val tvDateNaissance = findViewById<TextView>(R.id.dateNaissanceMedecin)
        val btnModifier = findViewById<Button>(R.id.btnModifier)
        val btnSupprimer = findViewById<Button>(R.id.btnSupprimer)

        lifecycleScope.launch {
            database.medecinDao().getMedecinById(medecinId).collect { medecin ->
                if (medecin != null) {
                    tvNom.text = medecin.nom
                    tvSpecialite.text = medecin.specialite
                    tvDateNaissance.text = medecin.dateNaissance
                    
                    medecin.photoPath?.let { path ->
                        try {
                            val file = File(path)
                            if (file.exists()) {
                                imgPhoto.setImageURI(Uri.fromFile(file))
                                imgPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                            }
                        } catch (e: Exception) {
                            imgPhoto.setImageResource(android.R.drawable.ic_menu_camera)
                        }
                    } ?: run {
                        imgPhoto.setImageResource(android.R.drawable.ic_menu_camera)
                    }

                    btnModifier.setOnClickListener {
                        val intent = Intent(this@ProfileActivity, AjouterMedecinActivity::class.java)
                        intent.putExtra("EDIT_MEDECIN_ID", medecin.id)
                        startActivity(intent)
                    }
                    
                    btnSupprimer.setOnClickListener {
                        showDeleteConfirmationDialog(medecin)
                    }
                } else {
                    finish()
                }
            }
        }

        setupNavigation(bottomNavigation)
    }

    private fun showDeleteConfirmationDialog(medecin: Medecin) {
        AlertDialog.Builder(this)
            .setTitle("Supprimer le médecin")
            .setMessage("Êtes-vous sûr de vouloir supprimer ${medecin.nom} ? Cette action est irréversible.")
            .setPositiveButton("Supprimer") { _, _ ->
                deleteMedecin(medecin)
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun deleteMedecin(medecin: Medecin) {
        val database = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            database.medecinDao().deleteMedecin(medecin)
            Toast.makeText(this@ProfileActivity, "Médecin supprimé", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupNavigation(bottomNavigation: BottomNavigationView) {
        bottomNavigation.setOnItemSelectedListener { item ->
            val intent = when (item.itemId) {
                R.id.nav_accueil -> Intent(this, DashboardActivity::class.java)
                R.id.nav_liste -> Intent(this, ListeMedecinsActivity::class.java)
                R.id.nav_ajouter -> Intent(this, AjouterMedecinActivity::class.java)
                else -> null
            }
            
            intent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(it)
                @Suppress("DEPRECATION")
                overridePendingTransition(0, 0)
                finish()
            }
            true
        }
    }
}
