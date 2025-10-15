package com.example.stratify

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.stratify.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class MainActivity : AppCompatActivity(), ProfileOptionsFragment.OnOptionSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation

        // This single line correctly handles all bottom navigation clicks
        // by navigating to the fragment with the same ID as the menu item.
        bottomNavigationView.setupWithNavController(navController)

        binding.appBar.accountIcon.setOnClickListener {
            val profileOptionsFragment = ProfileOptionsFragment()
            profileOptionsFragment.show(supportFragmentManager, ProfileOptionsFragment.TAG)
        }
    }

    override fun onProfileEditSelected() {
        navController.navigate(R.id.action_global_profileEditFragment)
    }

    override fun onAccountSelected() {
        navController.navigate(R.id.action_global_accountFragment)
    }

    /**
     * This function is added to satisfy the OnOptionSelectedListener interface.
     * You need to define what should happen when the "Languages" option is selected.
     */
    override fun onLanguagesSelected() {
        navController.navigate(R.id.action_global_languagesFragment)
    }

    private fun loadLocale() {
        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        if (language != null && language.isNotEmpty()) {
            setLocale(language)
        }
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}
