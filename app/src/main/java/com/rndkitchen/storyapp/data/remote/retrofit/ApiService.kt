package com.rndkitchen.storyapp.data.remote.retrofit

import com.rndkitchen.storyapp.data.remote.LoginRequest
import com.rndkitchen.storyapp.data.remote.RegisterBody
import com.rndkitchen.storyapp.data.remote.response.LoginResponse
import com.rndkitchen.storyapp.data.remote.response.PutStoryResponse
import com.rndkitchen.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    companion object {
        fun getApi(): ApiService? {
            return ApiConfig.client?.create(ApiService::class.java)
        }
    }
}

interface  StoryService {
    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String
    ): StoriesResponse

    @POST("register")
    suspend fun userRegister(
        @Body registerBody: RegisterBody
    ): Response<StoriesResponse>

    @Multipart
    @POST("stories")
    suspend fun putStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): PutStoryResponse

    @POST("login")
    suspend fun logInUser(
        @Body logInBody: LoginRequest
    ): LoginResponse
}