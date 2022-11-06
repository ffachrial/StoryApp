package com.rndkitchen.storyapp.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rndkitchen.storyapp.data.remote.RegisterBody
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.databinding.ActivityRegisterBinding
import com.rndkitchen.storyapp.ui.ViewModelFactory
import com.rndkitchen.storyapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    private val binding get() = activityRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

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

        binding.edRegisterPassword2.addTextChangedListener(object : TextWatcher {
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
        binding.registerButton.setOnClickListener {
            val nameUser = binding.edRegisterName.text.toString()
            val emailUser = binding.edRegisterEmail.text.toString()
            val passUser = binding.edRegisterPassword2.text.toString()
            val request = RegisterBody(nameUser, emailUser, passUser)

            userRegister(request)
        }

        binding.tvLogin.setOnClickListener {
            LoginActivity.start(this)
        }
    }

    private fun userRegister(regUser: RegisterBody) {
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

    private fun setCustomButtonEnable() {
        val result = binding.edRegisterPassword2.text
        binding.registerButton.isEnabled = (result != null) && result.toString().isNotEmpty()
    }

}