package com.rndkitchen.storyapp.ui.map

import androidx.lifecycle.ViewModel
import com.rndkitchen.storyapp.repository.StoriesRepository

class MapsViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {
    fun getCompletedStories(token: String, location: Int) = storiesRepository.getStoriesMap(token, location)
}