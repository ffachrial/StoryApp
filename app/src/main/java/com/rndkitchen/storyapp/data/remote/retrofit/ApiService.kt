package com.rndkitchen.storyapp.data.remote.retrofit

import com.rndkitchen.storyapp.data.remote.LoginRequest
import com.rndkitchen.storyapp.data.remote.RegisterBody
import com.rndkitchen.storyapp.data.remote.response.LoginResponse
import com.rndkitchen.storyapp.data.remote.response.PutStoryResponse
import com.rndkitchen.storyapp.data.remote.response.RegisterResponse
import com.rndkitchen.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface  ApiService {
    @GET("stories")
    suspend fun getStoriesPaging(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): StoriesResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 0,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): StoriesResponse

    @POST("register")
    suspend fun userRegister(
        @Body registerBody: RegisterBody
    ): RegisterResponse

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