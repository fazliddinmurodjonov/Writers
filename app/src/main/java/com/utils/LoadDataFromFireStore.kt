package com.utils

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.room.database.WriterDatabase
import com.room.entity.Writer

object LoadDataFromFireStore {

    fun loadData(context: Context) {
        val db = WriterDatabase.getInstance(context)
        val fireStore = FirebaseFirestore.getInstance()

        fireStore.collection("writers")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result
                    for (queryDocumentSnapshot in result) {
                        val w = queryDocumentSnapshot.toObject(Writer::class.java)
                        val writer = Writer(w.fullName,
                            w.yearBirth,
                            w.yearDied,
                            w.typeOfLiterature,
                            w.fullInformation,
                            w.image,
                            w.favourite)
                        db.writerDao().insertWriter(writer)
                    }
                }
            }

    }

    fun loadDataForNew(context: Context, listSize: Int) {
        val db = WriterDatabase.getInstance(context)
        val fireStore = FirebaseFirestore.getInstance()

        fireStore.collection("writers")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result
                    val size = result.size()
                    var count = 1
                    if (size > listSize) {
                        for (queryDocumentSnapshot in result) {
                            if (count > listSize) {
                                val w = queryDocumentSnapshot.toObject(Writer::class.java)
                                val writer = Writer(w.fullName,
                                    w.yearBirth,
                                    w.yearDied,
                                    w.typeOfLiterature,
                                    w.fullInformation,
                                    w.image,
                                    w.favourite)
                                db.writerDao().insertWriter(writer)
                            }
                            count++
                        }
                    }

                }
            }

    }
}
