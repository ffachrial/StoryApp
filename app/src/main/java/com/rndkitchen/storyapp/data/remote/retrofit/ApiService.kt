package com.rndkitchen.storyapp.data.remote.retrofit

import com.rndkitchen.storyapp.data.remote.LoginRequest
import com.rndkitchen.storyapp.data.remote.RegisterBody
import com.rndkitchen.storyapp.data.remote.response.LoginResponse
import com.rndkitchen.storyapp.data.remote.response.StoriesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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
}