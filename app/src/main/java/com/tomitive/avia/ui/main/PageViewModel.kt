package com.tomitive.avia.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<String>()
    val text: LiveData<String> = Transformations.map(_index) {
        it
    }

    fun setIndex(message: String?) {
        _index.value = message
    }
}