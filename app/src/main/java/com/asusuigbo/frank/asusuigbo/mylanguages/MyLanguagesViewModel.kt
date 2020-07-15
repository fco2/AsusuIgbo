package com.asusuigbo.frank.asusuigbo.mylanguages

import android.app.Application
import androidx.lifecycle.*
import com.asusuigbo.frank.asusuigbo.database.AsusuIgboDatabase
import com.asusuigbo.frank.asusuigbo.database.LanguageInfo
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoDao
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoRepository
import com.asusuigbo.frank.asusuigbo.helpers.DateHelper
import com.asusuigbo.frank.asusuigbo.helpers.LessonsHelper
import com.asusuigbo.frank.asusuigbo.models.DataInfo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class MyLanguagesViewModel(val app: Application) : AndroidViewModel(app) {
    private val authUserId = FirebaseAuth.getInstance().currentUser!!.uid
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private var repository : LanguageInfoRepository
    private var dao: LanguageInfoDao = AsusuIgboDatabase.getDatabase(app).languageInfoDao

    private val _dataList = MutableLiveData<List<DataInfo>>()
    val dataList: LiveData<List<DataInfo>>
        get() = _dataList

    init {
        repository = LanguageInfoRepository(authUserId, dao)
    }

    fun getAllLanguagesData(): LiveData<List<LanguageInfo>> {
        return repository.getAllLanguages()
    }

    fun setListData(it: List<LanguageInfo>) {
        val adaptedList = mutableListOf<DataInfo>()
        it.forEach {
            adaptedList.add(DataInfo(it.language, "", it.isActive))
        }
        adaptedList.add(DataInfo("+ Add Language", "Add Language", false))
        _dataList.value = adaptedList
    }

    fun setNewActiveLanguage(selectedLang: String){
        scope.launch {
            withContext(Main) {
                //deactivate all lang
                repository.updateAll(false)
                val lang = repository.getLanguage(selectedLang)
                if(lang != null){
                    repository.updateNewActive(selectedLang)
                    //repository.delete("Yoruba")
                }else{
                    repository.addLanguage(LanguageInfo(authUserId, selectedLang, true, DateHelper.getFormattedDate()))
                    //add lessons for that language since it is new
                    val userLessonList = LessonsHelper.createLessons()
                    val auth = FirebaseAuth.getInstance()
                    LessonsHelper.saveLessonsToFirebase(auth, userLessonList, selectedLang)
                }
            }
        }
    }
}