package com.asusuigbo.frank.asusuigbo.fragments.lessons

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asusuigbo.frank.asusuigbo.models.LessonInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LessonsViewModel(private val context: Context) : ViewModel(){
    private val _lessonsList = MutableLiveData<List<LessonInfo>>()
    val lessonsList: LiveData<List<LessonInfo>>
        get() = _lessonsList
    private val _viewableLessons = MutableLiveData<Int>()
    val viewableLessons : LiveData<Int>
        get() = _viewableLessons

    private val job = Job()
    private val scope = CoroutineScope(IO + job)

    init{
        scope.launch{
            populateDataList()
        }
    }

    private fun populateDataList(){

        val dbReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("TableOfContent")
        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = ArrayList<LessonInfo>()
                for (d in dataSnapshot.children){
                    val imageName: String = d.child("ImageDrawableIndex").value.toString()
                    val key = d.child("LessonKey").value.toString()
                    //TODO: change how image is stored
                    val resId: Int = context.resources.getIdentifier(imageName,"mipmap", context.packageName)
                    val item = LessonInfo(resId, key)
                    list.add(item)
                }
                _lessonsList.value = list
                val auth = FirebaseAuth.getInstance()
                //TODO: change how lessons done are stored
                val lessonsActivatedDbRef = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(auth.currentUser!!.uid)

                lessonsActivatedDbRef.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        _viewableLessons.value = p0.child("LessonsCompleted").value.toString().toInt()
                    }
                    override fun onCancelled(p0: DatabaseError) {
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //do nothing
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}