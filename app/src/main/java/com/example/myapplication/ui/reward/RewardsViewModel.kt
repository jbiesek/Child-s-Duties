package com.example.myapplication.ui.reward


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.RewardWithChildren
import com.example.myapplication.data.core.RewardRepository
import com.example.myapplication.ui.BaseViewModel
import com.example.myapplication.ui.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardsViewModel @Inject constructor(private val rewardRepository: RewardRepository) : BaseViewModel() {

    private val _rewardList = MutableLiveData<List<RewardWithChildren>>()
    val rewardList = _rewardList

    init {
        fetchRewardList()
    }

    private fun fetchRewardList() {
        viewModelScope.launch(Dispatchers.IO) {
            rewardRepository.getAllRewardsWithChildren.collect {
                _rewardList.postValue(it)
            }
        }
    }

    fun navigateToAddReward() {
        navigateTo(NavigationCommand.To(RewardsFragmentDirections.actionNavigationRewardsToRewardAddEditFragment()))
    }

    fun editReward(rewardWithChildren: RewardWithChildren) {
        navigateTo(NavigationCommand.To(RewardsFragmentDirections.actionNavigationRewardsToRewardAddEditFragment().setRewardId(rewardWithChildren.reward.rewardId)))
    }
}