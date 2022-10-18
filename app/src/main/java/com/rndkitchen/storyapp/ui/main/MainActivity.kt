package com.rndkitchen.storyapp.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rndkitchen.storyapp.data.remote.Result2
import com.rndkitchen.storyapp.data.remote.response.DataStories
import com.rndkitchen.storyapp.databinding.ActivityMainBinding
import com.rndkitchen.storyapp.ui.ViewModelFactory
import com.rndkitchen.storyapp.util.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private val binding get() = activityMainBinding

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
        val token = SessionManager.getToken(this)

        getStories("Bearer $token")
        binding.rvStories.layoutManager = LinearLayoutManager(this)
    }

    private fun getStories(token: String) {
        val storiesViewModel = obtainViewModel(this@MainActivity)

        storiesViewModel.getStories(token).observe(this) { response ->
            when (response) {
                is Result2.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result2.Success -> {
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
                is Result2.Error -> {
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
}
