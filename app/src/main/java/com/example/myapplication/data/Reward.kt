package com.example.myapplication.data

import androidx.room.*

@Entity
data class Reward(
    @PrimaryKey(autoGenerate = true) val rewardId: Long = 0,
    var name: String = "",
    var dutyPoints: Int = 0,
    var behaviourPoints: Int = 0,
    var isDeleted: Boolean = false
)

@Entity(primaryKeys = ["rewardId", "childId"])
data class RewardChildCrossRef(
    val rewardId: Long,
    val childId: Long
)

data class RewardWithChildren(
    @Embedded var reward: Reward = Reward(),

    @Relation(
        parentColumn = "rewardId",
        entityColumn = "childId",
        associateBy = Junction(
            value = RewardChildCrossRef::class)
    )
    var children: MutableList<Child> = mutableListOf()
)
