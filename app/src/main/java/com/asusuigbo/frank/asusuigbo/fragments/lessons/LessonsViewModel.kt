package com.asusuigbo.frank.asusuigbo.fragments.lessons

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asusuigbo.frank.asusuigbo.database.LanguageInfo
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoRepository
import com.asusuigbo.frank.asusuigbo.helpers.DateHelper
import com.asusuigbo.frank.asusuigbo.models.UserLesson
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LessonsViewModel @ViewModelInject constructor(private var repository : LanguageInfoRepository): ViewModel(){
    private val authUserId = FirebaseAuth.getInstance().currentUser!!.uid
    private val _lessonsList = MutableLiveData<List<UserLesson>>()
    val lessonsList: LiveData<List<UserLesson>>
        get() = _lessonsList

    var wordsLearned = "No"

    private val _activeLanguage = MutableLiveData<String>()
    val activeLanguage : LiveData<String>
        get() = _activeLanguage

    init{
        viewModelScope.launch{
            withContext(Main){
                val langInfo = repository.getActiveLanguage()
                if (langInfo != null)
                    _activeLanguage.value = langInfo.language
            }
            populateDataList()
        }
    }

    private fun insert(authUserId: String, languageInfo: LanguageInfo) = viewModelScope.launch {
        val repository = LanguageInfoRepository(authUserId, repository.dao)
        //repository.delete()
        repository.addLanguage(languageInfo)
    }

    private fun populateDataList(){
        val dbReference = FirebaseDatabase.getInstance().reference
        //check if user has un-installed app before, or if db has changed.
        if(activeLanguage.value == null){
            val languageRef = dbReference.child("Users/$authUserId/Language/")
            languageRef.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    for(lang in snapshot.children){
                        _activeLanguage.value = lang.key.toString()
                    }
                    val lessonRef = dbReference.child("Users/$authUserId/Language/${activeLanguage.value!!}/Lessons")

                        lessonRef.addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onCancelled(error: DatabaseError) {}

                            override fun onDataChange(snapshot: DataSnapshot) {
                                //get lessons for that language, then save lesson data to db using room.
                                val list = getLessons(snapshot)
                                //save room db data as new entry
                                val languageInfo = LanguageInfo(authUserId, activeLanguage.value!!,true, DateHelper.getFormattedDate())
                                insert(authUserId, languageInfo)
                                wordsLearned = "0"
                                _lessonsList.value = list
                            }
                        })
                }
            })
        }else{
            val userLessons = dbReference.child("Users/$authUserId/Language/${this.activeLanguage.value!!}/Lessons/")
            val wordsLearnedRef = dbReference.child("Users/$authUserId/WordsLearned")
            userLessons.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list = getLessons(dataSnapshot)

                    wordsLearnedRef.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {}

                        override fun onDataChange(snapshot: DataSnapshot) {
                            wordsLearned = snapshot.value.toString()
                            _lessonsList.value = list
                        }
                    })
                }
                override fun onCancelled(p0: DatabaseError) {}
            })
        }
    }

    private fun getLessons(dataSnapshot: DataSnapshot): ArrayList<UserLesson> {
        val list = ArrayList<UserLesson>()
        for (d in dataSnapshot.children) {
            val userLesson = d.getValue(UserLesson::class.java)!!
            userLesson.Index = d.key!!.toInt()
            list.add(userLesson)
        }
        return list
    }
}