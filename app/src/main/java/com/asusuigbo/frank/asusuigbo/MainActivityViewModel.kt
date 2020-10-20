package com.asusuigbo.frank.asusuigbo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel @ViewModelInject constructor(private val repository: LanguageInfoRepository): ViewModel() {
    private val _activeLanguage = MutableLiveData<String>()
    val activeLanguage : LiveData<String>
        get() = _activeLanguage

    init{
        viewModelScope.launch{
            withContext(Dispatchers.Main){
                val langInfo = repository.getActiveLanguage()
                if (langInfo != null)
                    _activeLanguage.value = langInfo.language
            }
        }
    }
}