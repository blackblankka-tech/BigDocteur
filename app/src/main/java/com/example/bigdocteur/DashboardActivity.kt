package com.example.bigdocteur

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.bigdocteur.data.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard2)

        val mainView = findViewById<View>(R.id.main)
        val header = findViewById<View>(R.id.header)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Top padding for header to avoid overlap with status bar (time, battery)
            header.setPadding(header.paddingLeft, systemBars.top, header.paddingRight, header.paddingBottom)
            // Bottom padding for navigation to avoid overlap with navigation bar
            bottomNavigation.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        val database = AppDatabase.getDatabase(this)
        val tvCount = findViewById<TextView>(R.id.tvCountMedecins)

        lifecycleScope.launch {
            database.medecinDao().getMedecinCount().collect { count ->
                tvCount.text = count.toString()
            }
        }

        setupNavigation(bottomNavigation)
    }

    private fun setupNavigation(bottomNavigation: BottomNavigationView) {
        bottomNavigation.selectedItemId = R.id.nav_accueil
        bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == bottomNavigation.selectedItemId) return@setOnItemSelectedListener true
            
            val intent = when (item.itemId) {
                R.id.nav_liste -> Intent(this, ListeMedecinsActivity::class.java)
                R.id.nav_ajouter -> Intent(this, AjouterMedecinActivity::class.java)
                else -> null
            }
            
            intent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(it)
                @Suppress("DEPRECATION")
                overridePendingTransition(0, 0)
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_accueil
    }
}
