package com.rndkitchen.storyapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rndkitchen.storyapp.data.local.entity.StoriesEntity

@Database(entities = [StoriesEntity::class], version = 1, exportSchema = false)
abstract class StoriesDatabase : RoomDatabase() {
    abstract fun storiesDao(): StoriesDao

    companion object {
        @Volatile
        private var instance: StoriesDatabase? = null
        fun getInstance(context: Context): StoriesDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoriesDatabase::class.java,
                    "Stories.db"
                ).build()
            }
    }
}