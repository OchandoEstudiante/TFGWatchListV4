package com.example.tfgwatchlist.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun setUserName(name:String){
        _userName.value = name
    }

    fun returnUserName(): String?{
        return _userName.value
    }
}