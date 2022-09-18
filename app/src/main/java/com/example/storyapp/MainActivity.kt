package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var customButton: CustomButton
    private lateinit var customEditText: CustomEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customButton = findViewById(R.id.login_button)
        customEditText = findViewById(R.id.ed_login_password)

        setCustomButtonEnable()

        customEditText.addTextChangedListener(object : TextWatcher {
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

        customButton.setOnClickListener { Toast.makeText(this@MainActivity, customEditText.text, Toast.LENGTH_SHORT).show() }
    }

    private fun setCustomButtonEnable() {
        val result = customEditText.text
        customButton.isEnabled = result != null && result.toString().isNotEmpty()
    }
}
