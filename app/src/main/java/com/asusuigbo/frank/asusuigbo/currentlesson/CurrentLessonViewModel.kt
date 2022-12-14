package com.asusuigbo.frank.asusuigbo.currentlesson

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentLessonViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
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

    private val _activeLanguage = MutableLiveData<String>()
    val activeLanguage : LiveData<String>
        get() = _activeLanguage

    private val _canAnswerQuestion = MutableLiveData<Boolean>()
    val canAnswerQuestion : LiveData<Boolean>
        get() = _canAnswerQuestion

    private val _isCorrect = MutableLiveData<Boolean>()
    val isCorrect : LiveData<Boolean>
        get() = _isCorrect

    private val _hasCorrectBeenSet = MutableLiveData<Boolean>()
    val hasCorrectBeenSet : LiveData<Boolean>
        get() = _hasCorrectBeenSet

    private val _playAudio = MutableLiveData<String>()
    val playAudio : LiveData<String>
        get() = _playAudio

    private val _resetBtnState = MutableLiveData<Boolean>()
    val resetBtnState : LiveData<Boolean>
        get() = _resetBtnState

    init{
        _canAnswerQuestion.value = false
        viewModelScope.launch {
            withContext(Main){
                _activeLanguage.value =  savedStateHandle.get<String>("language")
            }
            populateList()
        }
    }

    private fun populateList() {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val requestedLesson = savedStateHandle.get<String>("lessonName")
        val dbReference: DatabaseReference = database.getReference("Language/${activeLanguage.value}/Lessons/$requestedLesson")

        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dl = ArrayList<QuestionGroup>()
                for (d in dataSnapshot.children) {
                    val questionGroup = d.getValue(QuestionGroup::class.java)!!
                    dl.add(questionGroup)
                }
                _questionList.value = dl
                _listReady.value = true
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

    fun addQuestion(q: QuestionGroup){
        this._questionList.value!!.add(q)
    }

    fun setCanAnswerQuestion(){
        this._canAnswerQuestion.value = !this.canAnswerQuestion.value!!
    }

    fun setIsCorrect(f: Boolean){
        this._isCorrect.value = f
    }

    fun setHasCorrectBeenSet(f: Boolean){
        this._hasCorrectBeenSet.value = f
    }

    fun setPlayAudio(s: String){
        _playAudio.value = s
    }

    fun setResetBtnState(f: Boolean){
        _resetBtnState.value = f
    }
}