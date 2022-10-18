package com.rndkitchen.storyapp.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rndkitchen.storyapp.di.Injection
import com.rndkitchen.storyapp.repository.StoriesRepository

class RegisterViewModelFactory private constructor(private val storiesRepository: StoriesRepository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(storiesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: RegisterViewModelFactory? = null
        fun getInstance(context: Context): RegisterViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RegisterViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}