package com.asusuigbo.frank.asusuigbo.currentlesson

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class CurrentLessonViewModel(private var requestedLesson: String) : ViewModel() {
    //data list
    private val job = Job()
    private val scope = CoroutineScope(IO + job)

    private val _listReady = MutableLiveData<Boolean>()
    val listReady : LiveData<Boolean>
        get() = _listReady

    private val _questionList = MutableLiveData<ArrayList<QuestionGroup>>()
    val questionList : LiveData<ArrayList<QuestionGroup>>
        get() = _questionList
    private val _currentQuestion = MutableLiveData<QuestionGroup>()
    val currentQuestion : LiveData<QuestionGroup>
        get() = _currentQuestion
    private val _selectedAnswer = MutableLiveData<String>()
    val selectedAnswer : LiveData<String>
        get() = _selectedAnswer

    var dataListSize: Int = 0

    init{
        scope.launch {
            populateList()
        }
    }

    private fun populateList() {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val dbReference: DatabaseReference = database.getReference("Lessons/$requestedLesson")

        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val auth = FirebaseAuth.getInstance()
                val dl = ArrayList<QuestionGroup>()
                for (d in dataSnapshot.children) {
                    val questionGroup = d.getValue(QuestionGroup::class.java)!!
                    dl.add(questionGroup)
                    Timber.d("CHUKA - Got here")
                }
                dataListSize = dl.size
                dl.shuffle()
                _questionList.value = dl
                _listReady.value = true
                Timber.d("CHUKA size2 - ${_questionList.value!!.size}")

                //TODO: change the db reference to get user data
                /*database.reference.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        val temp = p0.child("Users").child(auth.currentUser!!.uid)
                            .child("WordsLearned").value.toString()
                        currentLessonActivity.wordsLearned = Integer.parseInt(temp)
                    }
                    override fun onCancelled(p0: DatabaseError) { }
                })*/
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setCurrentQuestion(){
        this._currentQuestion.value = this._questionList.value!!.removeAt(0)
    }

    fun setSelectedAnswer(s: String){
        this._selectedAnswer.value = s
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}