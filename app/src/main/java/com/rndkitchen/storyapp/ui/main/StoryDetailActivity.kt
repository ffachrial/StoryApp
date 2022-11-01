package com.rndkitchen.storyapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.rndkitchen.storyapp.data.remote.response.StoryResponse
import com.rndkitchen.storyapp.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var activityStoryDetailBinding: ActivityStoryDetailBinding
    private val binding get() = activityStoryDetailBinding

    private lateinit var story: StoryResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityStoryDetailBinding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(activityStoryDetailBinding.root)

        title = "Story Detail"
        story = intent.getParcelableExtra("extra_detail")!!

        binding.apply {
            Glide.with(ivDetailPhoto.context)
                .load(story.photoUrl)
                .into(ivDetailPhoto)

            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
        }
    }
}