package com.example.myapplication.data.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.data.*

@Database(
    entities = [Child::class,
    Reward::class,
    RewardChildCrossRef::class],
    version = 2,
    exportSchema = false
)

@TypeConverters(DateRoomConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun rewardDao(): RewardDao
}