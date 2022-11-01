package com.rndkitchen.storyapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rndkitchen.storyapp.data.local.model.StoriesModel

@Dao
interface NewStoriesDao {
    @Query("SELECT * FROM stories")
    fun getStories(): PagingSource<Int, StoriesModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<StoriesModel>)

    @Query("DELETE FROM stories")
    suspend fun deleteStories()

}