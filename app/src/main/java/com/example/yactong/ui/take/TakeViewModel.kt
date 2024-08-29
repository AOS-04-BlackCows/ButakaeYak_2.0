package com.example.yactong.ui.take

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TakeViewModel : ViewModel() {

    private val fragmentText : MutableLiveData<String> = MutableLiveData()

    fun getData(): MutableLiveData<String> = fragmentText

    fun updateItem(text :String) {
        fragmentText.value = text
    }

}