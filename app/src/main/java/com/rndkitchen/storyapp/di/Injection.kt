package com.rndkitchen.storyapp.di

import android.content.Context
import com.rndkitchen.storyapp.data.local.room.StoriesDatabase
import com.rndkitchen.storyapp.data.remote.retrofit.StoryConfig
import com.rndkitchen.storyapp.repository.StoriesRepository

object Injection {
    fun provideRepository(context: Context): StoriesRepository {
        val storyService = StoryConfig.getApiService()
        val database = StoriesDatabase.getInstance(context)
        val storiesDao = database.storiesDao()
        return StoriesRepository.getInstance(storyService, storiesDao)
    }
}