package com.blackcows.butakaeyak.ui.note

import androidx.lifecycle.ViewModel
import com.blackcows.butakaeyak.domain.repo.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val memoRepository: MemoRepository
): ViewModel() {

}