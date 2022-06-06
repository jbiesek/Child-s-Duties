package com.example.myapplication.ui.reward

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Child
import com.example.myapplication.data.Reward
import com.example.myapplication.data.RewardWithChildren
import com.example.myapplication.data.core.ChildRepository
import com.example.myapplication.data.core.RewardRepository
import com.example.myapplication.ui.BaseViewModel
import com.example.myapplication.ui.NavigationCommand
import com.example.myapplication.ui.SingleLiveEvent
import com.example.myapplication.ui.notifyObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardAddEditViewModel @Inject constructor(private val rewardRepository: RewardRepository, private val childRepository: ChildRepository) : BaseViewModel() {

    val deleteRewardDialogRequest = SingleLiveEvent<RewardWithChildren>()


    //#region Region Main elements
    fun initEmptyRewardWithChildren() {
        _reward.value = RewardWithChildren(reward = Reward(), children = mutableListOf())
    }

    override fun prepare(args: Bundle?) {
        super.prepare(args)

        val clear = args?.getBoolean("clear")
        val rewardId = args?.getLong("rewardId")
        if (clear == true) clearDataInModel()

        if (clear == false && reward.value != null)
            return

        if ((rewardId == null || rewardId == 0L)) {
            addMode = true
            initEmptyRewardWithChildren()
        } else {
            fetchReward(rewardId)
            addMode = false
        }
        fillSelectedIds()
    }

    private fun clearDataInModel() {
        reward.value = null
    }

    var addMode: Boolean = false

    //#endregion

    //#region Region Childs
    // lista dzieci do wyboru dla danego Duty.
    private val _childList = MutableLiveData<List<Child>>()
    val childList = _childList

    // lista wybranych (potrzebna tylko do wyświetlania (zaznaczenia)
    private val _childSelected = MutableLiveData(mutableListOf<Long>())
    val childSelected = _childSelected


    private fun fetchChildList() {
        viewModelScope.launch {
            childRepository.getChildren().collect {
                _childList.value = it
            }
        }
    }

    private fun fillSelectedIds() {
        if (reward.value == null) {
            _childSelected.value = mutableListOf()
        } else {
            var childIdList = reward.value!!.children?.map { child -> child.id }?.toMutableList()
            _childSelected.value  = childIdList
        }
    }

    //#endregion

    init {
        // pobranie listy dzieci do przypisania do duty
        fetchChildList()
    }


    //#region Region Reward

    private val _reward = MutableLiveData<RewardWithChildren>()
    val reward = _reward


    private fun fetchReward(rewardId: Long) {
        viewModelScope.launch {
            rewardRepository.getRewardWithChildren(rewardId).collect {
                _reward.value = it
                fillSelectedIds()
            }
        }
    }

    //#endregion


    //#region Region Actions

    fun addRewardOrEdit() {
        if (addMode) {
            if (reward.value != null && reward.value!!.reward.name.isNotEmpty()) {
                viewModelScope.launch(Dispatchers.IO) {
                    rewardRepository.addRewardWithChildren(_reward.value!!)
                    navigateTo(NavigationCommand.To(RewardAddEditFragmentDirections.actionRewardAddEditFragmentToNavigationRewards()))
                }
            } else {
                //TODO: show error null?
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                rewardRepository.updateRewardWithChildren(_reward.value!!)
                navigateTo(NavigationCommand.To(RewardAddEditFragmentDirections.actionRewardAddEditFragmentToNavigationRewards()))
            }
        }
    }


    fun cancelAddOrEdit() {
        navigateTo(NavigationCommand.To(RewardAddEditFragmentDirections.actionRewardAddEditFragmentToNavigationRewards()))
    }


    fun selectOrUnselectChild(childId: Long) {
        val child = childList.value!!.find { it -> it.id == childId }
        if (child != null) {
            if (!_childSelected.value!!.contains(childId)) {
                _reward.value!!.children!!.add(child)
            } else {
                _reward.value!!.children!!.remove(child)
            }
            fillSelectedIds()
        }

        _childSelected.notifyObserver()
    }

    fun askToDeleteReward() {
        if (reward.value != null)
            deleteRewardDialogRequest.postValue(reward.value)
    }

    fun deleteReward(rewardWithChildren: RewardWithChildren) {
        viewModelScope.launch(Dispatchers.IO) {
            rewardRepository.deactivateReward(rewardWithChildren.reward)
            cancelAddOrEdit()
        }
    }

    //#endregion

}