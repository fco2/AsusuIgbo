package com.asusuigbo.frank.asusuigbo.fragments.weeklynews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoRepository
import com.asusuigbo.frank.asusuigbo.helpers.Constants.getBeginningOfWeekDate
import com.asusuigbo.frank.asusuigbo.models.NewsInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class WeeklyNewsViewModel @ViewModelInject constructor(private var repository: LanguageInfoRepository): ViewModel() {

    private val _newsInfoList = MutableLiveData<MutableList<NewsInfo>>()
    val newsInfoList : LiveData<MutableList<NewsInfo>>
        get() = _newsInfoList

    private lateinit var activeLanguage: String
    init {
        viewModelScope.launch {
            activeLanguage = repository.getActiveLanguage()!!.language
            getWeeklyNews()
        }
    }

    private val _showNoNewsText = MutableLiveData<Boolean>()
    val showNoNewsText : LiveData<Boolean>
        get() = _showNoNewsText

    private fun getWeeklyNews(){
        val dbRef = FirebaseDatabase.getInstance().reference.child("Language/${activeLanguage}/WeeklyNews/${getBeginningOfWeekDate()}")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<NewsInfo>()
                for(child in snapshot.children){
                    val newsInfo = child.getValue(NewsInfo::class.java)!!
                    list.add(newsInfo)
                }
                if(list.size == 0){
                    setShowNoNewsText(true)
                }else{
                    _newsInfoList.value = list
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun setShowNoNewsText(t: Boolean){
        _showNoNewsText.value = t
    }
}