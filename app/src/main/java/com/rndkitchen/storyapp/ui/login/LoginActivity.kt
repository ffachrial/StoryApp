package com.rndkitchen.storyapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rndkitchen.storyapp.databinding.ActivityLoginBinding
import com.rndkitchen.storyapp.ui.main.MainActivity
import com.rndkitchen.storyapp.ui.register.RegisterActivity

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

        init()
        setCustomButtonEnable()

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
            MainActivity.start(this)
        }

        activityLoginBinding.tvRegister.setOnClickListener {
            RegisterActivity.start(this)
        }
    }

    private fun setCustomButtonEnable() {
        val result = activityLoginBinding.edLoginPassword.text
        activityLoginBinding.loginButton.isEnabled = (result != null) && result.toString().isNotEmpty()
    }
}