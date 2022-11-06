package com.rndkitchen.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rndkitchen.storyapp.utils.MainDispatcherRule
import com.rndkitchen.storyapp.repository.StoriesRepository
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.utils.DataDummy
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
class LogUserViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var logUserViewModel: LogUserViewModel
    private val dummyLogInResponse = DataDummy.generateDummyLoginResponse()
    private val dummyLogInRequest = DataDummy.generateDummyLoginRequest()

    @Before
    fun setUp() {
        logUserViewModel = LogUserViewModel(storiesRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when Get LoginResponse Should Not Null and Return Success`() = runTest {
        val expectedLoginResponse = flow {
            emit(Result.Success(dummyLogInResponse))
        }

        `when` (storiesRepository.userLogIn(dummyLogInRequest)).thenReturn(expectedLoginResponse)

        val actualLoginResponse = logUserViewModel.userLogIn(dummyLogInRequest).getOrAwaitValue()

        Mockito.verify(storiesRepository).userLogIn(dummyLogInRequest)
        assertNotNull(actualLoginResponse)
        assertTrue(actualLoginResponse is Result.Success)
    }
}