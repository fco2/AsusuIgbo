package com.asusuigbo.frank.asusuigbo.fragments.lessons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asusuigbo.frank.asusuigbo.models.UserLesson
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LessonsViewModel : ViewModel(){
    private val _lessonsList = MutableLiveData<List<UserLesson>>()
    val lessonsList: LiveData<List<UserLesson>>
        get() = _lessonsList

    private val job = Job()
    private val scope = CoroutineScope(IO + job)

    init{
        scope.launch{
            populateDataList()
        }
    }

    private fun populateDataList(){
        val auth = FirebaseAuth.getInstance()
        val dbReference = FirebaseDatabase.getInstance().reference
            .child("Users/${auth.currentUser!!.uid}/Lessons")
        dbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val list = ArrayList<UserLesson>()
                        for (d in dataSnapshot.children){
                            val userLesson = d.getValue(UserLesson::class.java)!!
                            userLesson.Index = d.key!!.toInt()
                            list.add(userLesson)
                        }
                        _lessonsList.value = list
                    }
                    override fun onCancelled(databaseError: DatabaseError) { }
                })
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}