package com.rndkitchen.storyapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rndkitchen.storyapp.data.remote.LoginRequest
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.databinding.ActivityLoginBinding
import com.rndkitchen.storyapp.ui.ViewModelFactory
import com.rndkitchen.storyapp.ui.main.MainActivity
import com.rndkitchen.storyapp.ui.register.RegisterActivity
import com.rndkitchen.storyapp.util.SessionManager

class   LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private val binding get() = activityLoginBinding

    private val logUserViewModel: LogUserViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToMain()
        }

        init()
        setCustomButtonEnable()

        binding.edLoginPassword2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setCustomButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun init() {
        binding.loginButton.setOnClickListener {
            doLogin()
        }

        binding.tvRegister.setOnClickListener {
            RegisterActivity.start(this)
        }
    }

    private fun setCustomButtonEnable() {
        val result = binding.edLoginPassword2.text
        binding.loginButton.isEnabled = (result != null) && result.toString().isNotEmpty()
    }

    private fun logInUser(loginRequest: LoginRequest) {
        logUserViewModel.userLogIn(loginRequest).observe(this) { response ->
            when (response) {
                is Result.Loading -> {
                    showLoading()
                }
                is Result.Success -> {
                    stopLoading()
                    if (response.data.loginResult.token.isNotEmpty()) {
                        response.data.loginResult.token.let { token ->
                            SessionManager.saveAuthToken(this, token)
                        }
                        navigateToMain()
                    }
                }
                is Result.Error -> {
                    stopLoading()
                    processError(response.error)
                }
            }
        }
    }

    private fun doLogin() {
        val email = binding.edLoginEmail.text.toString()
        val pwd = binding.edLoginPassword2.text.toString()
        val request = LoginRequest(email, pwd)

        logInUser(request)
    }

    private fun navigateToMain() {
        MainActivity.start(this)
        finish()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressBar.visibility = View.GONE
    }
}