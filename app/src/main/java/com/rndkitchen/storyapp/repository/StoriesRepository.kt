package com.rndkitchen.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.rndkitchen.storyapp.data.local.entity.StoriesEntity
import com.rndkitchen.storyapp.data.local.room.StoriesDao
import com.rndkitchen.storyapp.data.remote.Result2
import com.rndkitchen.storyapp.data.remote.retrofit.StoryService

class StoriesRepository private constructor(
    private val storyService: StoryService,
    private val storiesDao: StoriesDao
    ){
    fun getStories(token: String): LiveData<Result2<List<StoriesEntity>>> = liveData {
        emit(Result2.Loading)
        try {
            val response = storyService.getStories(token)
            val stories = response.listStory
            val listStories = stories.map { story ->
                StoriesEntity(
                    story.id,
                    story.name,
                    story.description,
                    story.photoUrl,
                    story.createdAt
                )
            }
            storiesDao.deleteStories()
            storiesDao.insertStory(listStories)
        } catch (e: Exception) {
            Log.d("StoriesRepository", "getStories: ${e.message.toString()} ")
            emit(Result2.Error(e.message.toString()))
        }
        val localData: LiveData<Result2<List<StoriesEntity>>> =
            storiesDao.getStories().map {
                Result2.Success(it)
            }
        emitSource(localData)
    }

    companion object {
        @Volatile
        private var instance: StoriesRepository? = null
        fun getInstance(
            storiesService: StoryService,
            storiesDao: StoriesDao
        ): StoriesRepository =
            instance ?: synchronized(this) {
                instance ?: StoriesRepository(storiesService, storiesDao)
            }.also { instance = it }
    }
}