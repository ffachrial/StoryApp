package com.rndkitchen.storyapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rndkitchen.storyapp.data.remote.response.StoryResponse

@Dao
interface StoryDao {
    @Query("SELECT * FROM user_stories")
    fun getStories(): PagingSource<Int, StoryResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(user_stories: List<StoryResponse>)

    @Query("DELETE FROM user_stories")
    suspend fun deleteStories()
}