package com.example.stratify

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stratify.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignupBinding

    private val passwordPattern = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Sign Up"

        auth = FirebaseAuth.getInstance()

        // Validasi password saat user mengetik
        setupPasswordValidation()

        binding.signInTextLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.signupButton.setOnClickListener {
            val email = binding.emailInputEdittext.text.toString().trim()
            val password = binding.passwordInputEdittext.text.toString().trim()
            val confirmPassword = binding.confirmPasswordInputEdittext.text.toString().trim()

            // Validasi: Pastikan semua field terisi
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi: Pastikan password dan konfirmasi password sama
            if (password != confirmPassword) {
                binding.passwordValidationText.visibility = View.VISIBLE
                binding.passwordValidationText.setTextColor(Color.RED)
                binding.passwordValidationText.text = "Passwords do not match."
                return@setOnClickListener
            }

            // Validasi: Pastikan password sesuai kriteria
            if (!passwordPattern.matcher(password).matches()) {
                binding.passwordValidationText.visibility = View.VISIBLE
                binding.passwordValidationText.setTextColor(Color.RED)
                binding.passwordValidationText.text = "Password is not strong enough."
                return@setOnClickListener
            }

            // Lanjutkan proses signup Firebase
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Sign up failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupPasswordValidation() {
        binding.passwordInputEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString().trim()
                if (password.isEmpty()) {
                    binding.passwordValidationText.visibility = View.INVISIBLE
                    return
                }
                binding.passwordValidationText.visibility = View.VISIBLE
                if (passwordPattern.matcher(password).matches()) {
                    binding.passwordValidationText.setTextColor(Color.GREEN)
                    binding.passwordValidationText.text = "Password is valid!"
                } else {
                    binding.passwordValidationText.setTextColor(Color.RED)
                    binding.passwordValidationText.text = "Password must be at least 8 characters long, and contain a lowercase letter, an uppercase letter, a number and a symbol."
                }
            }
        })
    }
}