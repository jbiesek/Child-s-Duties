package com.example.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao : BaseDao<Reward> {

    @Query("SELECT * FROM Reward WHERE NOT Reward.isDeleted ORDER BY name ASC")
    fun getRewards(): Flow<List<Reward>>

    @Transaction
    @Query("SELECT * FROM Reward WHERE NOT Reward.isDeleted")
    fun getRewardsWithChildren(): Flow<List<RewardWithChildren>>

    @Query("SELECT * FROM Reward WHERE rewardId = :rewardId and NOT Reward.isDeleted")
    fun getReward(rewardId: Long): Flow<Reward>

    @Transaction
    @Query("SELECT * FROM Reward WHERE rewardId = :rewardId and NOT Reward.isDeleted")
    fun getRewardWithChildren(rewardId: Long): Flow<RewardWithChildren>

    @Transaction
    fun addRewardWithChildren(reward: RewardWithChildren) {
        val rewardId = insert(reward.reward!!)

        deleteRewardChildCrossRef(rewardId = rewardId)

        var newCrossRef = mutableListOf<RewardChildCrossRef>()
        for (child in reward.children!!)
            newCrossRef.add(RewardChildCrossRef(rewardId = rewardId, childId = child.id))

        insert(newCrossRef)
    }

    @Transaction
    fun updateRewardWithChildren(reward: RewardWithChildren) {
        update(reward.reward!!)
        val rewardId = reward.reward!!.rewardId

        deleteRewardChildCrossRef(rewardId = rewardId)

        var newCrossRef = mutableListOf<RewardChildCrossRef>()
        for (child in reward.children!!)
            newCrossRef.add(RewardChildCrossRef(rewardId = rewardId, childId = child.id))

        insert(newCrossRef)
    }


    @Query("SELECT * FROM RewardChildCrossRef WHERE rewardId = :rewardId")
    fun getRewardChildCrossRef(rewardId: Int): List<RewardChildCrossRef>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(rewardChildCrossRef: RewardChildCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: List<RewardChildCrossRef>)

    @Query("DELETE FROM RewardChildCrossRef WHERE rewardId = :rewardId")
    fun deleteRewardChildCrossRef(rewardId: Long)

    @Query("SELECT * FROM Reward LEFT JOIN RewardChildCrossRef on Reward.rewardId = RewardChildCrossRef.rewardId where :childId = RewardChildCrossRef.childId and NOT Reward.isDeleted")
    fun getAllRewardsForChild(childId: Long) : Flow<List<RewardWithChildren>>

}