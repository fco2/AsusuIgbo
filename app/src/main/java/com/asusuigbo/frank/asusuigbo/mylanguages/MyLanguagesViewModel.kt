package com.asusuigbo.frank.asusuigbo.mylanguages

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asusuigbo.frank.asusuigbo.database.LanguageInfo
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoRepository
import com.asusuigbo.frank.asusuigbo.helpers.DateHelper
import com.asusuigbo.frank.asusuigbo.helpers.LessonsHelper
import com.asusuigbo.frank.asusuigbo.models.DataInfo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyLanguagesViewModel @ViewModelInject constructor(private var repository: LanguageInfoRepository) : ViewModel() {
    private val authUserId = FirebaseAuth.getInstance().currentUser!!.uid

    private val _dataList = MutableLiveData<List<DataInfo>>()
    val dataList: LiveData<List<DataInfo>>
        get() = _dataList

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
        viewModelScope.launch {
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