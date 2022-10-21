package com.rndkitchen.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndkitchen.storyapp.data.remote.LoginRequest
import com.rndkitchen.storyapp.data.remote.Result2
import com.rndkitchen.storyapp.data.remote.response.LoginResponse
import com.rndkitchen.storyapp.repository.StoriesRepository
import kotlinx.coroutines.launch

class LogUserViewModel(private val storiesRepository: StoriesRepository): ViewModel() {
    fun userLogIn(logInRequest: LoginRequest): LiveData<Result2<LoginResponse>> {
        val result = MutableLiveData<Result2<LoginResponse>>()
        viewModelScope.launch {
            storiesRepository.userLogIn(logInRequest).collect {
                result.postValue(it)
            }
        }
        return result
    }
}