package com.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.room.entity.Writer
import io.reactivex.rxjava3.core.Flowable

@Dao
interface WriterDao {
    @Insert
    fun insertWriter(writer: Writer)

    @Update
    fun updateWriter(writer: Writer)

    @Query("select *from Writer where id =  :writerId")
    fun getWriterById(writerId: Int): Writer

    @Query("select *from Writer where typeOfLiterature = :category")
    fun getWritersByCategory(category: Int): List<Writer>

    @Query("select *from Writer where typeOfLiterature = :category")
    fun getWritersByCategoryFlowable(category: Int): Flowable<List<Writer>>

    @Query("select *from Writer")
    fun getAllWriters(): List<Writer>

    @Query("select *from Writer")
    fun getWritersFlowable(): Flowable<List<Writer>>

}