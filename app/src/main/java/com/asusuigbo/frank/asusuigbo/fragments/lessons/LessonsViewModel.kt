package com.asusuigbo.frank.asusuigbo.fragments.lessons

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asusuigbo.frank.asusuigbo.database.AsusuIgboDatabase
import com.asusuigbo.frank.asusuigbo.database.LanguageInfo
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoDao
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoRepository
import com.asusuigbo.frank.asusuigbo.models.UserLesson
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LessonsViewModel(application: Application) : AndroidViewModel(application){
    private val authUserId = FirebaseAuth.getInstance().currentUser!!.uid
    private val _lessonsList = MutableLiveData<List<UserLesson>>()
    val lessonsList: LiveData<List<UserLesson>>
        get() = _lessonsList

    private val job = Job()
    private val scope = CoroutineScope(IO + job)
    private lateinit var repository : LanguageInfoRepository
    private var dao: LanguageInfoDao = AsusuIgboDatabase.getDatabase(application).languageInfoDao

    private val _activeLanguage = MutableLiveData<LanguageInfo>()
    val activeLanguage : LiveData<LanguageInfo>
        get() = _activeLanguage

    init{
        scope.launch{
            repository = LanguageInfoRepository(authUserId, dao)
            withContext(Main){
                _activeLanguage.value = repository.getActiveLanguage()
            }
            populateDataList()
        }
    }

    private fun populateDataList(){
        val dbReference = FirebaseDatabase.getInstance().reference
            .child("Users/$authUserId/Language/${this.activeLanguage.value!!.language}/Lessons/")
        dbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = ArrayList<UserLesson>()
                for (d in dataSnapshot.children){
                    val userLesson = d.getValue(UserLesson::class.java)!!
                    userLesson.Index = d.key!!.toInt()
                    list.add(userLesson)
                }
                _lessonsList.value = list
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}