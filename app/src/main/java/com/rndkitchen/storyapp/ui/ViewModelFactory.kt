package com.rndkitchen.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rndkitchen.storyapp.di.Injection
import com.rndkitchen.storyapp.repository.StoriesRepository
import com.rndkitchen.storyapp.ui.login.LogUserViewModel
import com.rndkitchen.storyapp.ui.main.PagingViewModel
import com.rndkitchen.storyapp.ui.storyadd.StoryAddViewModel
import com.rndkitchen.storyapp.ui.map.MapsViewModel
import com.rndkitchen.storyapp.ui.register.RegisterViewModel

class ViewModelFactory private constructor(private val storiesRepository: StoriesRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoryAddViewModel::class.java) -> StoryAddViewModel(storiesRepository) as T
            modelClass.isAssignableFrom(LogUserViewModel::class.java) -> LogUserViewModel(storiesRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(storiesRepository) as T
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> MapsViewModel(storiesRepository) as T
            modelClass.isAssignableFrom(PagingViewModel::class.java) -> PagingViewModel(storiesRepository) as T

           else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}