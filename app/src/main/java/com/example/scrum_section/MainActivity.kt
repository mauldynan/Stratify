package com.example.scrum_section

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scrum_section.ScrumFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SCRUM_SECTION) // ensure theme loaded before setContentView
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // load ScrumFragment as main content
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ScrumFragment())
                .commit()
        }
    }
}
