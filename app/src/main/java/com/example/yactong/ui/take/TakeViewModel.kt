package com.example.yactong.ui.take

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TakeViewModel : ViewModel() {

    //editText
    private val nameFragmentEditText : MutableLiveData<String> = MutableLiveData()

    //recyclerView textView
    private val formFragmentText : MutableLiveData<String?> = MutableLiveData()

    //viewPager2
    private val _currentPage = MutableLiveData<Int>()
    val currentPage : LiveData<Int> get() = _currentPage

    //timepicker dialog
    private val _selectedTimes = MutableLiveData<MutableList<Pair<Int, Int>>>().apply { value = mutableListOf() }
    val selectedTimes: LiveData<MutableList<Pair<Int, Int>>> get() = _selectedTimes

    //timepicker dialog 데이터 추가하기
    fun addTime(hour: Int, minute: Int) {
        val currentList = _selectedTimes.value ?: mutableListOf()
        currentList.add(Pair(hour, minute))
        _selectedTimes.value = currentList
    }

    //timepicker dialog 데이터 제거하기
    fun removeTime(position: Int) {
        val currentList = _selectedTimes.value ?: mutableListOf()
        if (position >= 0 && position < currentList.size) {
            currentList.removeAt(position)
            _selectedTimes.value = currentList
        }
    }

    //editText 데이터 받기
    fun getData(): MutableLiveData<String> = nameFragmentEditText

    //recyclerView Text 내용 넘기기
    fun getTextData(): MutableLiveData<String?> = formFragmentText

    //editText 내용 넘기기
    fun updateItem(text :String) {
        nameFragmentEditText.value = text
    }

    //recyclerView textView 내용 넘기기
    fun updateFormItem(text:String?){
        formFragmentText.value = text
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