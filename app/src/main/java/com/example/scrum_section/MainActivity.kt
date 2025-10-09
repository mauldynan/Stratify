// File: app/src/main/java/com/example/stratify/MainActivity.kt

package com.example.stratify // Corrected package name

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stratify.R // Make sure this is the correct R class
import com.example.scrum_section.ScrumFragment // Example: Assuming ScrumFragment is in a 'ui' sub-package. Adjust if necessary.

class ainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // The check for savedInstanceState is good practice to prevent re-adding the fragment on configuration changes.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ScrumFragment())
                .commit()
        }
    }
}
