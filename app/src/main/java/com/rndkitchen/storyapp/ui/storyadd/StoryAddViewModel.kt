package com.rndkitchen.storyapp.ui.storyadd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.data.remote.response.PutStoryResponse
import com.rndkitchen.storyapp.repository.StoriesRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryAddViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {

    fun putStory(token: String, file: MultipartBody.Part, description: RequestBody): LiveData<Result<PutStoryResponse>> {
        val result = MutableLiveData<Result<PutStoryResponse>>()
        viewModelScope.launch {
            storiesRepository.putStory(token, file, description).collect {
                result.postValue(it)
            }
        }
        return result
    }
}