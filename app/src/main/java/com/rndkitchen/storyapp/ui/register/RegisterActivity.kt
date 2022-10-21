package com.rndkitchen.storyapp.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rndkitchen.storyapp.data.remote.RegisterBody
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.databinding.ActivityRegisterBinding
import com.rndkitchen.storyapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    private val binding get() = activityRegisterBinding

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(activityRegisterBinding.root)

        init()
        setCustomButtonEnable()

        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count >= 6) {
                    setCustomButtonEnable()
                } else {
                    binding.edRegisterPassword.error = "Password less than 6 chars"
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }
        })
    }

    private fun init() {
        binding.registerButton.setOnClickListener {
            val nameUser = binding.edRegisterName.text.toString()
            val emailUser = binding.edRegisterEmail.text.toString()
            val passUser = binding.edRegisterPassword.text.toString()
            val request = RegisterBody(nameUser, emailUser, passUser)

            userRegister(request)
        }

        binding.tvLogin.setOnClickListener {
            LoginActivity.start(this)
        }
    }

    private fun userRegister(regUser: RegisterBody) {
        val registerViewModel = obtainViewModel(this@RegisterActivity)

        registerViewModel.userRegister(regUser).observe(this) { response ->
            when(response) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    LoginActivity.start(this)
                    Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity) : RegisterViewModel {
        val factory = RegisterViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[RegisterViewModel::class.java]
    }

    private fun setCustomButtonEnable() {
        val result = binding.edRegisterPassword.text
        binding.registerButton.isEnabled = (result != null) && result.toString().isNotEmpty()
    }

}