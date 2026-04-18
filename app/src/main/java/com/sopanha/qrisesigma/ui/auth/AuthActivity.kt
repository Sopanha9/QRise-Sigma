package com.sopanha.qrisesigma.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.sopanha.qrisesigma.databinding.ActivityAuthBinding
import com.sopanha.qrisesigma.ui.MainActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val auth = FirebaseAuth.getInstance()
    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateUiMode()

        binding.btnSubmit.setOnClickListener { handleSubmit() }
        binding.tvToggleMode.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUiMode()
        }
    }

    private fun updateUiMode() {
        if (isLoginMode) {
            binding.tvTitle.text = "Welcome Back"
            binding.tvSubtitle.text = "Sign in to QRise Sigma"
            binding.btnSubmit.text = "Sign In"
            binding.tvToggleMode.text = "Don't have an account? Sign up"
            binding.tilName.visibility = View.GONE
        } else {
            binding.tvTitle.text = "Create Account"
            binding.tvSubtitle.text = "Join QRise Sigma"
            binding.btnSubmit.text = "Create Account"
            binding.tvToggleMode.text = "Already have an account? Sign in"
            binding.tilName.visibility = View.VISIBLE
        }
    }

    private fun handleSubmit() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnSubmit.isEnabled = false

        if (isLoginMode) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { goToMain() }
                .addOnFailureListener { showError(it.message) }
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { goToMain() }
                .addOnFailureListener { showError(it.message) }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showError(msg: String?) {
        binding.progressBar.visibility = View.GONE
        binding.btnSubmit.isEnabled = true
        Toast.makeText(this, msg ?: "Something went wrong", Toast.LENGTH_LONG).show()
    }
}
