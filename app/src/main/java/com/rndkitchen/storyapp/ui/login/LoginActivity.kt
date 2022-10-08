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
import com.rndkitchen.storyapp.data.remote.response.BaseResponse
import com.rndkitchen.storyapp.data.remote.response.LoginResponse
import com.rndkitchen.storyapp.databinding.ActivityLoginBinding
import com.rndkitchen.storyapp.ui.main.MainActivity
import com.rndkitchen.storyapp.ui.register.RegisterActivity
import com.rndkitchen.storyapp.util.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private val viewModel : LoginViewModel by viewModels()

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

        viewModel.loginResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                    stopLoading()
                }
                else -> {
                    stopLoading()
                }
            }
        }

        activityLoginBinding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setCustomButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }
        })
    }

    private fun init() {
        activityLoginBinding.loginButton.setOnClickListener {
            doLogin()
        }

        activityLoginBinding.tvRegister.setOnClickListener {
            RegisterActivity.start(this)
        }
    }

    private fun setCustomButtonEnable() {
        val result = activityLoginBinding.edLoginPassword.text
        activityLoginBinding.loginButton.isEnabled = (result != null) && result.toString().isNotEmpty()
    }

    private fun processLogin(data: LoginResponse?) {
        showToast("Success:" + data?.message)
        if (!data?.loginResult?.token.isNullOrEmpty()) {
            data?.loginResult?.token?.let { SessionManager.saveAuthToken(this, it) }
            navigateToMain()
        }
    }

    private fun doLogin() {
        val email = activityLoginBinding.edLoginEmail.text.toString()
        val pwd = activityLoginBinding.edLoginPassword.text.toString()
        viewModel.loginUser(email = email, pwd = pwd)
    }

    private fun navigateToMain() {
        MainActivity.start(this)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun processError(msg: String?) {
        showToast("Error:$msg")
    }

    private fun showLoading() {
        activityLoginBinding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        activityLoginBinding.progressBar.visibility = View.GONE
    }
}