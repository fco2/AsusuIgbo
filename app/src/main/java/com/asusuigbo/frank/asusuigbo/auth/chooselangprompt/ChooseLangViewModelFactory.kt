package com.asusuigbo.frank.asusuigbo.auth.chooselangprompt

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ChooseLangViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ChooseLangViewModel::class.java)){
            return ChooseLangViewModel(application) as T
        }
        throw IllegalArgumentException("Wrong arguments provided")
    }
}