package com.rndkitchen.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndkitchen.storyapp.data.remote.RegisterBody
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.data.remote.response.RegisterResponse
import com.rndkitchen.storyapp.repository.StoriesRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val storiesRepository: StoriesRepository): ViewModel() {

    fun userRegister(regBody: RegisterBody): LiveData<Result<RegisterResponse>> {
        val result = MutableLiveData<Result<RegisterResponse>>()
        viewModelScope.launch {
            storiesRepository.userRegister(regBody).collect {
                result.postValue(it)
            }
        }
        return result
    }
}