package com.example.yactong.ui.take

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TakeViewModel : ViewModel() {

    //editText
    private val nameFragmentEditText : MutableLiveData<String> = MutableLiveData()

    //viewPager2
    private val _currentPage = MutableLiveData<Int>()
    val currentPage : LiveData<Int> get() = _currentPage

    //editText 데이터 받기
    fun getData(): MutableLiveData<String> = nameFragmentEditText

    //editText 내용 넘기기
    fun updateItem(text :String) {
        nameFragmentEditText.value = text
    }

    fun moveToNextPage(){
        _currentPage.value = (_currentPage.value ?: 0) + 1
    }

    fun moveToPreviousPage() {
        val current = _currentPage.value ?: 0
        if (current > 0) {
            _currentPage.value = current - 1
        }
    }

}