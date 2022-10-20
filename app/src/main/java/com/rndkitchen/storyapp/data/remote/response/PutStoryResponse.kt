package com.rndkitchen.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class PutStoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)
