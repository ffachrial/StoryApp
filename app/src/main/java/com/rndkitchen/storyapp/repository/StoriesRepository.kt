package com.rndkitchen.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.rndkitchen.storyapp.data.local.entity.StoriesEntity
import com.rndkitchen.storyapp.data.local.room.StoriesDao
import com.rndkitchen.storyapp.data.remote.RegisterBody
import com.rndkitchen.storyapp.data.remote.Result2
import com.rndkitchen.storyapp.data.remote.response.StoriesResponse
import com.rndkitchen.storyapp.data.remote.response.PutStoryResponse
import com.rndkitchen.storyapp.data.remote.retrofit.StoryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

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

    suspend fun userRegister(authBody: RegisterBody): Flow<Result2<Response<StoriesResponse>>> {
        return flow {
            try {
                emit(Result2.Loading)
                val response = storyService.userRegister(authBody)
                if (response.code() == 201) {
                    emit(Result2.Success(response))
                } else if (response.code() == 400) {
                    val errorBody = org.json.JSONObject(response.errorBody()!!.string())
                    emit(Result2.Error(errorBody.getString("message")))
                }
            } catch (ex: Exception) {
                emit(Result2.Error(ex.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun putStory(token: String, file: MultipartBody.Part, description: RequestBody): Flow<Result2<PutStoryResponse>> {
        return flow {
            try {
                emit(Result2.Loading)
                val response = storyService.putStory(token, file, description)
                if (!response.error) {
                    emit(Result2.Success(response))
                } else {
                    emit(Result2.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result2.Error(ex.message.toString()))
            }
        }
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