package com.rndkitchen.storyapp.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rndkitchen.storyapp.databinding.ActivityRegisterBinding
import com.rndkitchen.storyapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    private val viewModel : RegisterViewModel by viewModels()

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

        activityRegisterBinding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
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

        //customButton.setOnClickListener { Toast.makeText(this@RegisterActivity, customEditText.text, Toast.LENGTH_SHORT).show() }
    }

    private fun init() {
        activityRegisterBinding.registerButton.setOnClickListener {
            LoginActivity.start(this)
        }
    }

    private fun setCustomButtonEnable() {
        val result = activityRegisterBinding.edRegisterPassword.text
        activityRegisterBinding.registerButton.isEnabled = (result != null) && result.toString().isNotEmpty()
    }

}