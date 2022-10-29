package com.rndkitchen.storyapp.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.data.remote.response.DataStories
import com.rndkitchen.storyapp.databinding.ActivityMainBinding
import com.rndkitchen.storyapp.ui.ViewModelFactory
import com.rndkitchen.storyapp.ui.login.LoginActivity
import com.rndkitchen.storyapp.util.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private val binding get() = activityMainBinding
    private var token: String? = null

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        token = SessionManager.getToken(this)

        binding.actionLogout.setOnClickListener {
            val pref = SessionManager
            pref.clearData(this)
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        binding.openMap.setOnClickListener {
            MapsActivity.start(this)
        }

        binding.addStory.setOnClickListener {
            StoryAddActivity.start(this)
        }

        getStories("Bearer $token")
        binding.rvStories.layoutManager = LinearLayoutManager(this)
    }

    private fun getStories(token: String) {
        val storiesViewModel = obtainViewModel(this@MainActivity)

        storiesViewModel.getStories(token).observe(this) { response ->
            when (response) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val stories = response.data
                    val dataStories = stories.map {
                        DataStories(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                            photoUrl = it.photoUrl,
                            createdAt = it.createdAt
                        )
                    }
                    val adapter = StoriesAdapter(dataStories)
                    binding.rvStories.adapter = adapter
                    binding.progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity) : StoriesViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[StoriesViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        getStories("Bearer $token")
    }
}
