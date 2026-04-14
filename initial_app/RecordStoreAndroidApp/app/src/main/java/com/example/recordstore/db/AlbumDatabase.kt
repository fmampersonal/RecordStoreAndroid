package com.example.recordstore.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recordstore.dao.AlbumDao
import com.example.recordstore.entity.Album

@Database(entities = [Album::class], version = 1, exportSchema = false)
abstract class AlbumDatabase : RoomDatabase() {

    abstract val albumDao: AlbumDao

    companion object {
        @Volatile
        private var INSTANCE: AlbumDatabase? = null

        fun getInstance(context: Context): AlbumDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AlbumDatabase::class.java,
                    "record_store_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}