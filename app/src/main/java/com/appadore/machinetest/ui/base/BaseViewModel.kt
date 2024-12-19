package com.appadore.machinetest.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appadore.machinetest.data.ProgressState
import kotlinx.coroutines.launch


abstract class BaseViewModel : ViewModel() {
    var actionLiveData: MutableLiveData<Int> = MutableLiveData()

    protected var TAG: String = javaClass.simpleName
    val sessionLiveData = MutableLiveData<Boolean>()
    val progressLiveData: MutableLiveData<ProgressState> = MutableLiveData()
    private val _messagesLiveData: MutableLiveData<String> = MutableLiveData()
    val messagesLiveData: LiveData<String> get() = _messagesLiveData

    fun setProgressState(isShow: Boolean, which: Int = -1) {
        viewModelScope.launch {
            progressLiveData.value = ProgressState(state = which, isShow = isShow)
        }
    }

    fun setSuccessMessage(message: String?) {
        message?.let {
            _messagesLiveData.value = it
        }
    }

    fun setErrorMessage(message: String?) {
        message?.let {
            _messagesLiveData.value = it
        }
    }

    fun setAction(action: Int) {
        actionLiveData.value = action
    }

}