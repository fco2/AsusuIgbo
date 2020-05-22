package com.asusuigbo.frank.asusuigbo.fragments.lessons

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class LessonsViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LessonsViewModel::class.java))
            return LessonsViewModel(context) as T
        throw IllegalArgumentException("wrong arguments provided")
    }

}