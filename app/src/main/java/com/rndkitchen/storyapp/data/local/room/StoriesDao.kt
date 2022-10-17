package com.rndkitchen.storyapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rndkitchen.storyapp.data.local.entity.StoriesEntity

@Dao
interface StoriesDao {
    @Query("SELECT * FROM stories")
    fun getStories(): LiveData<List<StoriesEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(stories: List<StoriesEntity>)

    @Query("DELETE FROM stories")
    suspend fun deleteStories()
}