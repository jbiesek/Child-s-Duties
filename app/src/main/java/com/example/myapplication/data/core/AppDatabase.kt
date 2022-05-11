package com.example.myapplication.data.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.data.Child
import com.example.myapplication.data.ChildDao

@Database(
    entities = [Child::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(DateRoomConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
}