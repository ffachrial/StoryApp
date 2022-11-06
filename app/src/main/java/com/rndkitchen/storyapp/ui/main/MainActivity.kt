package com.rndkitchen.storyapp.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rndkitchen.storyapp.adapter.LoadingStateAdapter
import com.rndkitchen.storyapp.adapter.StoriesListAdapter
import com.rndkitchen.storyapp.databinding.ActivityMainBinding
import com.rndkitchen.storyapp.ui.ViewModelFactory
import com.rndkitchen.storyapp.ui.login.LoginActivity
import com.rndkitchen.storyapp.ui.map.MapsActivity
import com.rndkitchen.storyapp.ui.storyadd.StoryAddActivity
import com.rndkitchen.storyapp.util.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private val binding get() = activityMainBinding
    private var token: String? = null

    private val pagingViewModel: PagingViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

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

        getStoriesPaging("Bearer $token")
    }

    private fun getStoriesPaging(token: String) {
        val adapter = StoriesListAdapter()

        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        binding.rvStories.layoutManager = LinearLayoutManager(this)

        pagingViewModel.getStoriesPaging(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onResume() {
        super.onResume()
        getStoriesPaging("Bearer $token")
    }
}
