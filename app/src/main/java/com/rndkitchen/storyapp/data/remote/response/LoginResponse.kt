package com.rndkitchen.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("loginResult")
    val loginResult: User
) {
    data class User(
        @SerializedName("userId")
        val userId: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("token")
        val token: String
    )
}
