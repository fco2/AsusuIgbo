package com.asusuigbo.frank.asusuigbo.currentlesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class CurrentLessonViewModelFactory(private var requestedLesson: String, private var activeLang: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CurrentLessonViewModel::class.java))
            return CurrentLessonViewModel(requestedLesson, activeLang) as T
        throw IllegalArgumentException("Wrong arguments provided")
    }
}