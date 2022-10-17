package com.rndkitchen.storyapp.repository

import com.rndkitchen.storyapp.data.remote.retrofit.ApiService
import com.rndkitchen.storyapp.data.remote.LoginRequest
import com.rndkitchen.storyapp.data.remote.response.LoginResponse
import retrofit2.Response

class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>? {
        return  ApiService.getApi()?.loginUser(loginRequest = loginRequest)
    }
}