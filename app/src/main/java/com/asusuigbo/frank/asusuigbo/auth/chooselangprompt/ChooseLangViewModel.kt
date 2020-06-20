package com.asusuigbo.frank.asusuigbo.auth.chooselangprompt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asusuigbo.frank.asusuigbo.database.AsusuIgboDatabase
import com.asusuigbo.frank.asusuigbo.database.LanguageInfo
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoDao
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class ChooseLangViewModel(application: Application) : AndroidViewModel(application) {
    //co-routine
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)
    private var repository : LanguageInfoRepository

    private var dao: LanguageInfoDao = AsusuIgboDatabase.getDatabase(application).languageInfoDao

    private val _activeLanguage = MutableLiveData<LanguageInfo>()
    val activeLanguage : LiveData<LanguageInfo>
        get() = _activeLanguage

    init{
        repository = LanguageInfoRepository(dao)
        getActiveLanguage()
    }

    private fun getActiveLanguage() = coroutineScope.launch {
        withContext(Main){
            _activeLanguage.value = repository.getActiveLanguage()
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}