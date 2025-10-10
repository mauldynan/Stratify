package com.example.stratify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.stratify.databinding.ActivityMainBinding // Menggunakan binding yang sesuai dengan layout container
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Menggunakan ActivityMainAppContainerBinding
    private lateinit var navController: NavController

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inisialisasi View Binding dan atur layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Dapatkan NavController dari NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 3. Dapatkan referensi BottomNavigationView dari binding
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation // Menggunakan binding untuk BottomNavigationView

        // 4. Hubungkan BottomNavigationView dengan NavController untuk Fragment (Home & Scrum)
        bottomNavigationView.setupWithNavController(navController)

        // 5. Tambahan Logika Khusus untuk Navigasi Antar Activity
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Navigasi ke Home Fragment
                    navController.navigate(item.itemId)
                    true
                }
                R.id.navigation_scrum -> {
                    // Navigasi ke Scrum Fragment
                    navController.navigate(item.itemId)
                    true
                }
                R.id.navigation_workspace -> {
                    // ✅ Navigasi Khusus: Pindah ke MainWorkspace Activity
                    val intent = Intent(this, MainWorkspace::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
