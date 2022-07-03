package com.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.room.entity.WriterFavourite
import io.reactivex.rxjava3.core.Flowable

@Dao
interface WriterFavouriteDao {
    @Insert
    fun insertWriter(writerFavourite: WriterFavourite)

    @Delete
    fun deleteWriter(writerFavourite: WriterFavourite)

    @Query("select *from WriterFavourite where id = :id")
    fun getWriterById(id: Int): WriterFavourite

    @Query("select *from WriterFavourite")
    fun getWriters(): List<WriterFavourite>

    @Query("select *from WriterFavourite")
    fun getWritersFlowable(): Flowable<List<WriterFavourite>>

}