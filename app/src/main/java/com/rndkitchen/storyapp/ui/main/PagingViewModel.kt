package com.rndkitchen.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rndkitchen.storyapp.data.remote.response.StoryResponse
import com.rndkitchen.storyapp.repository.StoriesRepository

class PagingViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {
    fun getStoriesPaging(token: String): LiveData<PagingData<StoryResponse>> = storiesRepository.getStoriesPaging(token).cachedIn(viewModelScope)
}