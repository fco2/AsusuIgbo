package com.asusuigbo.frank.asusuigbo.auth.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asusuigbo.frank.asusuigbo.database.AsusuIgboDatabase
import com.asusuigbo.frank.asusuigbo.database.LanguageInfo
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoDao
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    //co-routine
    private val job = Job()
    private val coroutineScope = CoroutineScope(IO + job)

    private val _language = MutableLiveData<String>()
    val language : LiveData<String>
        get() = _language
    private var dao: LanguageInfoDao = AsusuIgboDatabase.getDatabase(application).languageInfoDao

    fun insert(authUserId: String, languageInfo: LanguageInfo) = coroutineScope.launch {
        val repository = LanguageInfoRepository(authUserId, dao)
        //repository.delete()
        repository.addLanguage(languageInfo)
    }

    fun setLanguage(lang: String){
        this._language.value = lang
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}