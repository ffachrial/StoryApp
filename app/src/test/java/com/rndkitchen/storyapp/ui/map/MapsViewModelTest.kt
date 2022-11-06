package com.rndkitchen.storyapp.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rndkitchen.storyapp.data.local.entity.StoriesEntity
import com.rndkitchen.storyapp.repository.StoriesRepository
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.utils.getOrAwaitValue
import com.rndkitchen.storyapp.utils.DataDummy
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
class MapsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyStories = DataDummy.generateDummyStoriesEntity()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storiesRepository)
    }

    @Test
    fun `when Get CompletedStories Should Not Null and Return Success`() {
        val expectedStories = MutableLiveData<Result<List<StoriesEntity>>>()
        expectedStories.value = Result.Success(dummyStories)

        `when` (storiesRepository.getStoriesMap(token = "", location = 1)).thenReturn(expectedStories)

        val actualStories = mapsViewModel.getCompletedStories(token = "", location = 1).getOrAwaitValue()

        Mockito.verify(storiesRepository).getStoriesMap(token = "", location = 1)
        assertNotNull(actualStories)
        assertTrue(actualStories is Result.Success)
        assertEquals(dummyStories.size, (actualStories as Result.Success).data.size)
    }
}