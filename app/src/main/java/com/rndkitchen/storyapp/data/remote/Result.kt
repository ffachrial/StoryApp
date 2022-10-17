package com.rndkitchen.storyapp.data.remote

sealed class Result<out T> {
    data class Success<out T>(val data: T? = null) : Result<T>()
    data class Loading(val nothing: Nothing? = null) : Result<Nothing>()
    data class Error(val msg: String?) : Result<Nothing>()
}

sealed class Result2<out R> private constructor() {
    data class Success<out T>(val data: T) : Result2<T>()
    data class Error(val error: String) : Result2<Nothing>()
    object Loading : Result2<Nothing>()
}
