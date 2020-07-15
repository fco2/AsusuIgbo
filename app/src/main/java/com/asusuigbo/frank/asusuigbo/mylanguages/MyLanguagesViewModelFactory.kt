package com.asusuigbo.frank.asusuigbo.mylanguages

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class MyLanguagesViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyLanguagesViewModel::class.java))
            return MyLanguagesViewModel(app) as T
        throw IllegalArgumentException("Wrong arguments provided!")
    }
}