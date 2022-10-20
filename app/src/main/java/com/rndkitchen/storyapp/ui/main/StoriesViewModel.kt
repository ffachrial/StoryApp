package com.rndkitchen.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndkitchen.storyapp.data.remote.Result2
import com.rndkitchen.storyapp.data.remote.response.PutStoryResponse
import com.rndkitchen.storyapp.repository.StoriesRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoriesViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {
    fun getStories(token: String) = storiesRepository.getStories(token)
    fun putStory(token: String, file: MultipartBody.Part, description: RequestBody): LiveData<Result2<PutStoryResponse>> {
        val result = MutableLiveData<Result2<PutStoryResponse>>()
        viewModelScope.launch {
            storiesRepository.putStory(token, file, description).collect {
                result.postValue(it)
            }
        }
        return result
    }
}