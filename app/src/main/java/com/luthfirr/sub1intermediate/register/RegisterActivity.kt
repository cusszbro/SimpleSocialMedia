package com.luthfirr.sub1intermediate.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.luthfirr.sub1intermediate.R
import com.luthfirr.sub1intermediate.databinding.ActivityRegisterBinding
import com.luthfirr.sub1intermediate.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            registerAction()
            registerViewModel.getRegister().observe(this) {
                if (it.error == true) {
                    showLoading(false)
                    Toast.makeText(
                        this@RegisterActivity,
                        getString(R.string.email_already),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showLoading(false)
                    Toast.makeText(this@RegisterActivity, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun registerAction() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val password2 = binding.passwordEditText2.text.toString()
        when {
            name.isEmpty() && email.isEmpty() && password.isEmpty() && password2.isEmpty() -> {
                binding.nameEditText.error = getString(R.string.fill_name)
                binding.emailEditText.error = getString(R.string.fill_email)
                binding.passwordEditText.error = getString(R.string.fill_password)
                binding.passwordEditText2.error = getString(R.string.fill_password)
            }
            name.isEmpty() -> {
                binding.nameEditText.error = getString(R.string.fill_name)
            }
            email.isEmpty() -> {
                binding.emailEditText.error = getString(R.string.fill_email)
            }
            password.isEmpty() -> {
                binding.passwordEditText.error = getString(R.string.fill_password)
            }
            password2.isEmpty() -> {
                binding.passwordEditText2.error = getString(R.string.fill_password)
            }
            password2.length < 6 -> {
                binding.passwordEditText2.error = getString(R.string.password_less)
            }
            password.length < 6 -> {
                binding.passwordEditText.error = getString(R.string.password_less)
            }
            password != password2 -> {
                binding.passwordEditText2.error = getString(R.string.same_password)
            }
            name.isEmpty() && password.isEmpty() -> {
                binding.nameEditText.error = getString(R.string.fill_name)
                binding.passwordEditText.error = getString(R.string.fill_password)
            }
            else -> {
                showLoading(true)
                registerViewModel.setRegister(name, email, password)
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditText = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val passwordEditText2 = ObjectAnimator.ofFloat(binding.passwordEditText2, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, nameEditText, emailEditText, passwordEditText, passwordEditText2, register)
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}