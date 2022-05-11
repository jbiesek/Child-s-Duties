package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Child
import com.example.myapplication.data.core.ChildRepository
import com.example.myapplication.ui.BaseViewModel
import com.example.myapplication.ui.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val childRepository: ChildRepository): BaseViewModel() {

    private val _childList = MutableLiveData<List<Child>>()
    val childList : LiveData<List<Child>> = _childList

    init {
        fetchChildList()
    }

    private fun fetchChildList() {
        viewModelScope.launch {
            childRepository.getChildren().collect { _childList.postValue(it) }
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun addNewChild(){
        var direction = HomeFragmentDirections.actionNavigationHomeToChildAddEditFragment()
        navigateTo(NavigationCommand.To(direction))
    }
}