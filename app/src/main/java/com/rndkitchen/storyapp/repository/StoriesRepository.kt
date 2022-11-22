package com.rndkitchen.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rndkitchen.storyapp.data.local.entity.StoriesEntity
import com.rndkitchen.storyapp.data.local.room.StoriesDao
import com.rndkitchen.storyapp.data.paging3.StoriesPagingSource
import com.rndkitchen.storyapp.data.remote.LoginRequest
import com.rndkitchen.storyapp.data.remote.RegisterBody
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.data.remote.response.*
import com.rndkitchen.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoriesRepository constructor(
    private val storiesService: ApiService,
    private val storiesDao: StoriesDao
    ){
    fun getStoriesMap(token: String, location: Int): LiveData<Result<List<StoriesEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = storiesService.getStories(token, location)
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
            //Log.d("StoriesRepository", "getStories: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<StoriesEntity>>> =
            storiesDao.getStories().map {
                Result.Success(it)
            }
        emitSource(localData)
    }

    suspend fun userRegister(authBody: RegisterBody): Flow<Result<RegisterResponse>> {
        return flow {
            try {
                emit(Result.Loading)
                val response = storiesService.userRegister(authBody)
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
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
                val response = storiesService.putStory(token, file, description)
                Log.d("putStory_file", "file + $file")
                Log.d("putStory_description", "description + $description")
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
                val response = storiesService.logInUser(logInBody)
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(Result.Error(ex.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getStoriesPaging(token: String): LiveData<PagingData<StoryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(storiesService, token)
            }
        ).liveData
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