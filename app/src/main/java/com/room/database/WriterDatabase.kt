package com.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.room.dao.WriterDao
import com.room.dao.WriterFavouriteDao
import com.room.entity.Writer
import com.room.entity.WriterFavourite

@Database(entities = [Writer::class, WriterFavourite::class], version = 1)
abstract class WriterDatabase : RoomDatabase() {
    abstract fun writerDao(): WriterDao
    abstract fun writerFavouriteDao(): WriterFavouriteDao

    companion object {
        private var instance: WriterDatabase? = null

        @Synchronized
        fun getInstance(context: Context): WriterDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context,
                        WriterDatabase::class.java,
                        "writer_db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }

    }
}