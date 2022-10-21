package com.rndkitchen.storyapp.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rndkitchen.storyapp.di.Injection
import com.rndkitchen.storyapp.repository.StoriesRepository

class LogUserViewModelFactory private constructor(private val storiesRepository: StoriesRepository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogUserViewModel::class.java)) {
            return LogUserViewModel(storiesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: LogUserViewModelFactory? = null
        fun getInstance(context: Context): LogUserViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LogUserViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}