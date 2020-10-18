package com.asusuigbo.frank.asusuigbo.auth.signup

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asusuigbo.frank.asusuigbo.database.LanguageInfo
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoDao
import kotlinx.coroutines.launch

class SignUpViewModel @ViewModelInject constructor(private var dao: LanguageInfoDao) : ViewModel() {

    private val _language = MutableLiveData<String>()
    val language : LiveData<String>
        get() = _language

    fun insert(languageInfo: LanguageInfo) = viewModelScope.launch {
        //repository.delete()
        dao.addLanguage(languageInfo)
    }

    fun setLanguage(lang: String){
        this._language.value = lang
    }
}