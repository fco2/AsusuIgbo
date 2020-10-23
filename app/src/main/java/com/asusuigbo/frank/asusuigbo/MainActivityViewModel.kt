package com.asusuigbo.frank.asusuigbo

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

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

    // from myLanguages
    private val authUserId = FirebaseAuth.getInstance().currentUser!!.uid
    private val _languagesList = MutableLiveData<List<DataInfo>>()
    val languagesList: LiveData<List<DataInfo>>
        get() = _languagesList

    fun getAllLanguagesData(): LiveData<List<LanguageInfo>> {
        return repository.getAllLanguages()
    }

    fun setListData(it: List<LanguageInfo>) {
        val adaptedList = mutableListOf<DataInfo>()
        it.forEach {
            adaptedList.add(DataInfo(it.language, "", it.isActive))
        }
        adaptedList.add(DataInfo("+ Add Language", "Add Language", false))
        _languagesList.value = adaptedList
    }

    fun setNewActiveLanguage(selectedLang: String){
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
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
                _activeLanguage.value = selectedLang
            }
        }
    }

}