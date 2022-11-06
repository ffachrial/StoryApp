package com.rndkitchen.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rndkitchen.storyapp.repository.StoriesRepository
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.utils.DataDummy
import com.rndkitchen.storyapp.utils.MainDispatcherRule
import com.rndkitchen.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyRegisterBody = DataDummy.generateDummyRegisterBody()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(storiesRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when Get RegisterResponse Should Not Null and Return Success`() = runTest {
        val expectedRegisterResponse = flow {
            emit(Result.Success(dummyRegisterResponse))
        }

        `when` (storiesRepository.userRegister(dummyRegisterBody)).thenReturn(expectedRegisterResponse)

        val actualRegisterResponse = registerViewModel.userRegister(dummyRegisterBody).getOrAwaitValue()

        Mockito.verify(storiesRepository).userRegister(dummyRegisterBody)
        assertNotNull(actualRegisterResponse)
        assertTrue(actualRegisterResponse is Result.Success)
    }
}