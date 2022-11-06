package com.rndkitchen.storyapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rndkitchen.storyapp.data.local.entity.StoriesEntity
import com.rndkitchen.storyapp.data.remote.response.StoryResponse

@Database(
    entities = [StoryResponse::class, StoriesEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoriesDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun storiesDao(): StoriesDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var instance: StoriesDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoriesDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoriesDatabase::class.java,
                    "Stories.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
    }
}