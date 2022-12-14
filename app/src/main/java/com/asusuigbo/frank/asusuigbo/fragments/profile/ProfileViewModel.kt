package com.asusuigbo.frank.asusuigbo.fragments.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asusuigbo.frank.asusuigbo.database.LanguageInfo
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoRepository
import com.asusuigbo.frank.asusuigbo.models.DataInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel @ViewModelInject constructor(repository: LanguageInfoRepository) : ViewModel() {
    lateinit var username: String
    private val authUserId = FirebaseAuth.getInstance().currentUser!!.uid

    private val _dataList = MutableLiveData<List<DataInfo>>()
    val dataList: LiveData<List<DataInfo>>
        get() = _dataList
    private val _dateStarted = MutableLiveData<String>()
    val dateStarted : LiveData<String>
        get() = _dateStarted
    private val _activeLang = MutableLiveData<String>()
    val activeLang : LiveData<String>
        get() = _activeLang

    init {
        setListData()
        viewModelScope.launch{
            withContext(Dispatchers.Main){
                val languageInfo = repository.getActiveLanguage()
                if(languageInfo != null){
                   setUsername(languageInfo)
                }
            }
        }
    }

    private fun setListData() {
        val list = mutableListOf<DataInfo>()
        list.add(DataInfo(("Edit Profile")))
        list.add(DataInfo(("Languages")))
        list.add(DataInfo(("Friends")))
        list.add(DataInfo(("Settings")))
        list.add(DataInfo(("Help")))
        _dataList.value = list
    }

    private fun setUsername(languageInfo: LanguageInfo) {
        val dbReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        dbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                username = p0.child("Users").child(authUserId).child("Username").value.toString()
                _dateStarted.value = languageInfo.startDate
                _activeLang.value = languageInfo.language
            }
        })
    }
}