package com.example.bigdocteur

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.bigdocteur.data.AppDatabase
import com.example.bigdocteur.data.Medecin
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class AjouterMedecinActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private var internalImagePath: String? = null
    private lateinit var imgPhoto: ImageView
    private var isEditMode = false
    private var existingMedecinId: String? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imgPhoto.setImageURI(it)
            // Remove the teal tint so the photo appears with its real colors
            imgPhoto.imageTintList = null 
            imgPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
            internalImagePath = saveImageToInternalStorage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_medecin2)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val header = findViewById<View>(R.id.header)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            bottomNavigation.setPadding(0, 0, 0, systemBars.bottom)
            header.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        imgPhoto = findViewById(R.id.imgPhotoMedecin)
        val etId = findViewById<TextInputEditText>(R.id.etIdMedecin)
        val etNom = findViewById<TextInputEditText>(R.id.etNomMedecin)
        val etSpecialite = findViewById<TextInputEditText>(R.id.etSpecialite)
        val etDateNaissance = findViewById<TextInputEditText>(R.id.etDateNaissance)
        val btnEnregistrer = findViewById<Button>(R.id.btnEnregistrer)
        val titrePage = findViewById<TextView>(R.id.titrePage)

        val database = AppDatabase.getDatabase(this)

        etDateNaissance.setOnClickListener {
            showDatePicker(etDateNaissance)
        }

        existingMedecinId = intent.getStringExtra("EDIT_MEDECIN_ID")
        if (existingMedecinId != null) {
            isEditMode = true
            titrePage.text = "Modifier le Profil"
            btnEnregistrer.text = "Mettre à jour"
            etId.isEnabled = false

            lifecycleScope.launch {
                database.medecinDao().getMedecinById(existingMedecinId!!).collect { medecin ->
                    medecin?.let {
                        etId.setText(it.id)
                        etNom.setText(it.nom)
                        etSpecialite.setText(it.specialite)
                        etDateNaissance.setText(it.dateNaissance)
                        it.photoPath?.let { path ->
                            internalImagePath = path
                            try {
                                val file = File(path)
                                if (file.exists()) {
                                    imgPhoto.setImageURI(Uri.fromFile(file))
                                    imgPhoto.imageTintList = null // Clear tint for existing photo
                                    imgPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                                }
                            } catch (e: Exception) {
                                imgPhoto.setImageResource(android.R.drawable.ic_menu_camera)
                            }
                        }
                    }
                }
            }
        }

        findViewById<com.google.android.material.card.MaterialCardView>(R.id.cardPhoto).setOnClickListener {
            pickImage.launch("image/*")
        }

        btnEnregistrer.setOnClickListener {
            val id = etId.text.toString()
            val nom = etNom.text.toString()
            val specialite = etSpecialite.text.toString()
            val dateNaissance = etDateNaissance.text.toString()

            if (id.isNotEmpty() && nom.isNotEmpty() && specialite.isNotEmpty() && dateNaissance.isNotEmpty()) {
                if (validateAge(dateNaissance)) {
                    showConfirmationDialog(id, nom, specialite, dateNaissance)
                } else {
                    showAgeErrorDialog()
                }
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        setupNavigation(bottomNavigation)
    }

    private fun validateAge(dateStr: String): Boolean {
        return try {
            val parts = dateStr.split("/")
            val day = parts[0].toInt()
            val month = parts[1].toInt() - 1
            val year = parts[2].toInt()

            val birthDate = Calendar.getInstance()
            birthDate.set(year, month, day)

            val today = Calendar.getInstance()

            var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

            if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                age--
            }

            age >= 18
        } catch (e: Exception) {
            false
        }
    }

    private fun showDatePicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            editText.setText(date)
        }, year, month, day)
        dpd.show()
    }

    private fun showAgeErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("Âge insuffisant")
            .setMessage("Le médecin doit avoir au moins 18 ans pour être enregistré.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showConfirmationDialog(id: String, nom: String, spec: String, date: String) {
        val title = if (isEditMode) "Confirmer la modification" else "Confirmer l'ajout"
        val message = if (isEditMode) "Voulez-vous mettre à jour ce profil ?" else "Voulez-vous enregistrer ce nouveau médecin ?"
        
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Oui") { _, _ ->
                saveMedecin(id, nom, spec, date)
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun saveMedecin(id: String, nom: String, spec: String, date: String) {
        val medecin = Medecin(id, nom, spec, date, internalImagePath)
        val database = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            database.medecinDao().insertMedecin(medecin)
            val message = if (isEditMode) "Profil mis à jour !" else "Médecin enregistré !"
            Toast.makeText(this@AjouterMedecinActivity, message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(filesDir, "medecin_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    private fun setupNavigation(bottomNavigation: BottomNavigationView) {
        bottomNavigation.selectedItemId = R.id.nav_ajouter
        bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == bottomNavigation.selectedItemId && !isEditMode) return@setOnItemSelectedListener true
            
            val intent = when (item.itemId) {
                R.id.nav_accueil -> Intent(this, DashboardActivity::class.java)
                R.id.nav_liste -> Intent(this, ListeMedecinsActivity::class.java)
                R.id.nav_ajouter -> if (isEditMode) Intent(this, AjouterMedecinActivity::class.java) else null
                else -> null
            }
            
            intent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(it)
                @Suppress("DEPRECATION")
                overridePendingTransition(0, 0)
                if (isEditMode) finish()
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isEditMode) {
            findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_ajouter
        }
    }
}
