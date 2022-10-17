package com.rndkitchen.storyapp.ui.main

import androidx.lifecycle.ViewModel
import com.rndkitchen.storyapp.repository.StoriesRepository

class StoriesViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {
    fun getStories(token: String) = storiesRepository.getStories(token)
}