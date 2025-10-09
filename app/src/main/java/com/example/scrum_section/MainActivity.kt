package com.example.scrum_section

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.stratify.R
import com.example.stratify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // We are replacing setupWithNavController with a manual listener
        // to gain more control and fix the navigation issue.

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            // This helper method correctly handles the back stack, ensuring that
            // when you switch between bottom navigation items, it doesn't just
            // stack fragments on top of each other.
            NavigationUI.onNavDestinationSelected(item, navController)
            true
        }

        // Since we removed setupWithNavController, we need to manually update the
        // selected item in the BottomNavigationView when the destination changes.
        // This is important for handling back button presses and other navigation events.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val menu = binding.bottomNavigation.menu
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                if (item.itemId == destination.id) {
                    item.isChecked = true
                }
            }
        }
    }
}
