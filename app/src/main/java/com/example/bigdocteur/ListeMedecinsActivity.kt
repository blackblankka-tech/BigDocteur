package com.example.bigdocteur

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bigdocteur.data.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ListeMedecinsActivity : AppCompatActivity() {

    private lateinit var adapter: MedecinAdapter
    private val searchQuery = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_medecins2)

        val mainView = findViewById<View>(R.id.main)
        val header = findViewById<View>(R.id.header)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            header.setPadding(header.paddingLeft, systemBars.top, header.paddingRight, header.paddingBottom)
            bottomNavigation.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        val database = AppDatabase.getDatabase(this)
        val rvMedecins = findViewById<RecyclerView>(R.id.listeMedecins)
        val searchView = findViewById<SearchView>(R.id.barreRecherche)

        adapter = MedecinAdapter { medecin ->
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("MEDECIN_ID", medecin.id)
            startActivity(intent)
        }
        rvMedecins.layoutManager = LinearLayoutManager(this)
        rvMedecins.adapter = adapter

        lifecycleScope.launch {
            searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isEmpty()) {
                        database.medecinDao().getAllMedecins()
                    } else {
                        database.medecinDao().searchMedecins("%$query%")
                    }
                }
                .collect { medecins ->
                    adapter.submitList(medecins)
                }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery.value = newText ?: ""
                return true
            }
        })

        setupNavigation(bottomNavigation)
    }

    private fun setupNavigation(bottomNavigation: BottomNavigationView) {
        bottomNavigation.selectedItemId = R.id.nav_liste
        bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == bottomNavigation.selectedItemId) return@setOnItemSelectedListener true
            
            val intent = when (item.itemId) {
                R.id.nav_accueil -> Intent(this, DashboardActivity::class.java)
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
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_liste
    }
}
