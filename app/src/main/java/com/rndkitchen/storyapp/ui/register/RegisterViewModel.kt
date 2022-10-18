package com.rndkitchen.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndkitchen.storyapp.data.remote.RegisterBody
import com.rndkitchen.storyapp.data.remote.Result2
import com.rndkitchen.storyapp.data.remote.response.StoriesResponse
import com.rndkitchen.storyapp.repository.StoriesRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel(private val storiesRepository: StoriesRepository): ViewModel() {

    fun userRegister(regBody: RegisterBody): LiveData<Result2<Response<StoriesResponse>>> {
        val result = MutableLiveData<Result2<Response<StoriesResponse>>>()
        viewModelScope.launch {
            storiesRepository.userRegister(regBody).collect {
                result.postValue(it)
            }
        }
        return result
    }

}