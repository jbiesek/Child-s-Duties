package com.example.myapplication.data.core

import com.example.myapplication.data.Reward
import com.example.myapplication.data.RewardDao
import com.example.myapplication.data.RewardWithChildren
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RewardRepository @Inject constructor(private val rewardDao: RewardDao) {

    val getAllRewards: Flow<List<Reward>> = rewardDao.getRewards()
    val getAllRewardsWithChildren: Flow<List<RewardWithChildren>> = rewardDao.getRewardsWithChildren()

    fun getReward(id: Long) = rewardDao.getReward(rewardId = id)
    fun getRewardWithChildren(id: Long) = rewardDao.getRewardWithChildren(rewardId = id)

    fun addReward(reward: Reward) = rewardDao.insert(reward)
    fun updateReward(reward: Reward) = rewardDao.update(reward)

    fun addRewardWithChildren(rewardWithChildren: RewardWithChildren) = rewardDao.addRewardWithChildren(rewardWithChildren)

    fun updateRewardWithChildren(rewardWithChildren: RewardWithChildren) = rewardDao.updateRewardWithChildren(rewardWithChildren)

    fun getAllRewardsForChild(childId: Long) = rewardDao.getAllRewardsForChild(childId)

    fun deactivateReward(reward: Reward) {
        reward.isDeleted = true
        updateReward(reward)
    }

}