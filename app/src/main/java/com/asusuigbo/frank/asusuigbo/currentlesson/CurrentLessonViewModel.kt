package com.asusuigbo.frank.asusuigbo.currentlesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentLessonViewModel(private var requestedLesson: String, activeLang: String) : ViewModel() {
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

    private val _activeLanguage = MutableLiveData<String>()
    private val activeLanguage : LiveData<String>
        get() = _activeLanguage

    private val _buttonState = MutableLiveData<UserButton>()
    val buttonState : LiveData<UserButton>
        get() = _buttonState

    private val _checkAnswer = MutableLiveData<Boolean>()
    val checkAnswer : LiveData<Boolean>
        get() = _checkAnswer

    private val _isCorrect = MutableLiveData<Boolean>()
    val isCorrect : LiveData<Boolean>
        get() = _isCorrect

    private val _hasCorrectBeenSet = MutableLiveData<Boolean>()
    val hasCorrectBeenSet : LiveData<Boolean>
        get() = _hasCorrectBeenSet

    init{
        _buttonState.value = UserButton.AnswerNotSelected
        _checkAnswer.value = false
        scope.launch {
            withContext(Main){
                _activeLanguage.value = activeLang
            }
            populateList()
        }
    }

    private fun populateList() {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val dbReference: DatabaseReference = database.getReference("Language/${activeLanguage.value}/Lessons/$requestedLesson")

        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val auth = FirebaseAuth.getInstance()
                val dl = ArrayList<QuestionGroup>()
                for (d in dataSnapshot.children) {
                    val questionGroup = d.getValue(QuestionGroup::class.java)!!
                    dl.add(questionGroup)
                }
                //dl.shuffle()
                _questionList.value = dl
                _listReady.value = true
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

    fun addQuestion(q: QuestionGroup){
        this._questionList.value!!.add(q)
    }

    fun setButtonState(b: UserButton){
        this._buttonState.value = b
    }

    fun setCheckAnswer(){
        this._checkAnswer.value = !this.checkAnswer.value!!
    }

    fun setIsCorrect(f: Boolean){
        this._isCorrect.value = f
    }

    fun setHasCorrectBeenSet(f: Boolean){
        this._hasCorrectBeenSet.value = f
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}