package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(child: Child) : Long

    @Query("SELECT * FROM Child")
    fun getChildren() : Flow<List<Child>>
}