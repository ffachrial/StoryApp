package com.rndkitchen.storyapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rndkitchen.storyapp.R
import com.rndkitchen.storyapp.databinding.ActivityStoriesBinding

class StoriesActivity : AppCompatActivity() {
    private lateinit var activityStoryBinding: ActivityStoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityStoryBinding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(activityStoryBinding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        activityStoryBinding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(activityStoryBinding.tabs, activityStoryBinding.viewPager) {
            tab: TabLayout.Tab, position: Int ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.stories, R.string.add_story)

        fun start(context: Context) {
            val intent = Intent(context, StoriesActivity::class.java)
            context.startActivity(intent)
        }
    }
}