package com.example.myapplication.data;

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Child (
    @PrimaryKey(autoGenerate = true) val id : Long = 0L,
    var name: String,
    var behaviourPoints: Int = 0,
    var dutyPoints: Int = 0,
    var drawableName: String = "",
    var birthday: Date
)