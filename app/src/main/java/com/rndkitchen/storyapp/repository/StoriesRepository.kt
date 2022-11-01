package com.rndkitchen.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.rndkitchen.storyapp.data.local.entity.StoriesEntity
import com.rndkitchen.storyapp.data.local.room.StoriesDao
import com.rndkitchen.storyapp.data.remote.LoginRequest
import com.rndkitchen.storyapp.data.remote.RegisterBody
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.data.remote.response.LoginResponse
import com.rndkitchen.storyapp.data.remote.response.StoriesResponse
import com.rndkitchen.storyapp.data.remote.response.PutStoryResponse
import com.rndkitchen.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class StoriesRepository private constructor(
    private val storyService: ApiService,
    private val storiesDao: StoriesDao
    ){
    fun getStoriesMap(token: String, location: Int): LiveData<Result<List<StoriesEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = storyService.getStories(token, location)
            val stories = response.listStory
            val listStories = stories.map { story ->
                StoriesEntity(
                    story.id,
                    story.name,
                    story.description,
                    story.photoUrl,
                    story.lat,
                    story.lon,
                    story.createdAt
                )
            }
            storiesDao.deleteStories()
            storiesDao.insertStory(listStories)
        } catch (e: Exception) {
            Log.d("StoriesRepository", "getStories: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<StoriesEntity>>> =
            storiesDao.getStories().map {
                Result.Success(it)
            }
        emitSource(localData)
    }

    fun getStories(token: String): LiveData<Result<List<StoriesEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = storyService.getStories(token)
            val stories = response.listStory
            val listStories = stories.map { story ->
                StoriesEntity(
                    story.id,
                    story.name,
                    story.description,
                    story.photoUrl,
                    story.lat,
                    story.lon,
                    story.createdAt
                )
            }
            storiesDao.deleteStories()
            storiesDao.insertStory(listStories)
        } catch (e: Exception) {
            Log.d("StoriesRepository", "getStories: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<StoriesEntity>>> =
            storiesDao.getStories().map {
                Result.Success(it)
            }
        emitSource(localData)
    }

    suspend fun userRegister(authBody: RegisterBody): Flow<Result<Response<StoriesResponse>>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = storyService.userRegister(authBody)
                if (response.code() == 201) {
                    emit(Result.Success(response))
                } else if (response.code() == 400) {
                    val errorBody = org.json.JSONObject(response.errorBody()!!.string())
                    emit(Result.Error(errorBody.getString("message")))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun putStory(token: String, file: MultipartBody.Part, description: RequestBody): Flow<Result<PutStoryResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = storyService.putStory(token, file, description)
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
    }

    suspend fun userLogIn(logInBody: LoginRequest): Flow<Result<LoginResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = storyService.logInUser(logInBody)
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }
    }

    companion object {
        @Volatile
        private var instance: StoriesRepository? = null
        fun getInstance(
            storiesService: ApiService,
            storiesDao: StoriesDao
        ): StoriesRepository =
            instance ?: synchronized(this) {
                instance ?: StoriesRepository(storiesService, storiesDao)
            }.also { instance = it }
    }
}