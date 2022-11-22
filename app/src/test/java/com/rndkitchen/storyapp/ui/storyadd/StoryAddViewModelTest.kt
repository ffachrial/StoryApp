package com.rndkitchen.storyapp.ui.storyadd

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
class StoryAddViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var addViewModel: StoryAddViewModel
    private val dummyPutStoryResponse = DataDummy.generateDummyPutStoryResponse()
    private val dummyUploadImage = DataDummy.generateDummyUploadImg()
    private val dummyUploadDec = DataDummy.generateDummyUploadDesc()

    @Before
    fun setUp() {
        addViewModel = StoryAddViewModel(storiesRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when PutStoryResponse Should Not Null and Return Success`() = runTest {
        val expectedPutStoryResponse = flow {
            emit(Result.Success(dummyPutStoryResponse))
        }

        `when` (storiesRepository.putStory(token = "", dummyUploadImage, dummyUploadDec)).thenReturn(expectedPutStoryResponse)

        val actualPutStoryResponse = addViewModel.putStory(token = "", dummyUploadImage, dummyUploadDec).getOrAwaitValue()

        Mockito.verify(storiesRepository).putStory(token = "", dummyUploadImage, dummyUploadDec)
        assertNotNull(actualPutStoryResponse)
        assertTrue(actualPutStoryResponse is Result.Success)
    }
}