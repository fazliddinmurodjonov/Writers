package com.utils

import com.example.connectionlesson_5_firebase_storage.App
import com.room.database.WriterDatabase

object CreateDB {
    val db = WriterDatabase.getInstance(App.instance)
}