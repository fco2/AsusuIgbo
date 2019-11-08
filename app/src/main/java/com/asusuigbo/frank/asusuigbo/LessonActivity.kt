package com.asusuigbo.frank.asusuigbo

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.asusuigbo.frank.asusuigbo.adapters.BuildSentenceViewAdapter
import com.asusuigbo.frank.asusuigbo.adapters.ImageChoiceViewAdapter
import com.asusuigbo.frank.asusuigbo.adapters.SingleSelectViewAdapter
import com.asusuigbo.frank.asusuigbo.adapters.WrittenTextViewAdapter
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.connection.helpers.PopupHelper
import com.asusuigbo.frank.asusuigbo.fragments.LessonCompletedFragment
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.roundToInt

class LessonActivity : AppCompatActivity() {
     var lastUpdatedWL: Int = 0
     var lastUpdatedLC: Int = 0
     var dataListSize: Int = 0
    var dataList: ArrayList<QuestionGroup> = ArrayList()
    var progressBar: ProgressBar? = null
    var requestedLesson: String = ""
    var activity: Activity = this
    var lessonsLayout: RelativeLayout? = null
    var button: Button? = null
    private var popUpWindow: PopupWindow? = null
    lateinit var currentQuestion: QuestionGroup
    var buttonState: UserButton = UserButton.AnswerNotSelected
    private var lessonStatusProgressBar: ProgressBar? = null
    var selectedAnswer = ""
    private var lessonCount = 0
    lateinit var singleSelectViewAdapter: SingleSelectViewAdapter
    //needs to be initialized within init{] method that is why this one is here
    lateinit var buildSentenceViewAdapter: BuildSentenceViewAdapter
    lateinit var writtenTextViewAdapter: WrittenTextViewAdapter
    lateinit var  imgChoiceViewAdapter: ImageChoiceViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        this.singleSelectViewAdapter = SingleSelectViewAdapter(this)
        this.writtenTextViewAdapter = WrittenTextViewAdapter(this)
        this.buildSentenceViewAdapter  = BuildSentenceViewAdapter(this)
        this.imgChoiceViewAdapter = ImageChoiceViewAdapter(this)
        this.button = findViewById(R.id.check_answer_button_id)
        this.progressBar = findViewById(R.id.progress_bar_lesson_id)
        this.lessonStatusProgressBar = findViewById(R.id.lesson_progress_id)
        this.lessonsLayout = findViewById(R.id.lessons_layout_id)
        this.progressBar!!.visibility = View.VISIBLE
        this.setLessonData()
        DataLoader.populateList(this)
        this.button!!.setOnClickListener(buttonClickListener)
    }

    private fun setLessonData(){
        this.requestedLesson = intent.getStringExtra("LESSON_NAME")
        this.lessonCount = intent.getIntExtra("LESSON_COUNT", 0)
    }

    private val buttonClickListener = View.OnClickListener {
        when(this.buttonState){
            UserButton.AnswerSelected -> {
                answerQuestion()
            }
            UserButton.NextQuestion -> {
                nextQuestion()
            }
            else -> {
                finishQuiz()
            }
        }
    }

    private fun answerQuestion(){
        this.currentQuestion = this.dataList.removeAt(0)
        disableOptions()
        this.popUpWindow = PopupHelper.displaySelectionInPopUp(this, isCorrectAnswer())

        if(!this.isCorrectAnswer())
            this.dataList.add(this.currentQuestion)
        if(this.dataList.size > 0)
            this.setUpButtonStateAndText(UserButton.NextQuestion, R.string.next_question_text)
        else
            this.setUpButtonStateAndText(UserButton.Finished, R.string.continue_text)
    }

    private fun nextQuestion(){
        this.popUpWindow!!.dismiss()
        this.updateOptions()
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
        setProgressBarStatus()
    }

    private fun disableOptions(){
        when {
            this.currentQuestion.LessonFormat == "SingleSelect" -> {
                this.singleSelectViewAdapter.disableOptions()
            }
            this.currentQuestion.LessonFormat == "MultiSelect" -> {
                this.buildSentenceViewAdapter.disableOptions()
            }
            this.currentQuestion.LessonFormat == "ImageSelect" -> {
                this.imgChoiceViewAdapter.disableOptions()
            }
            else -> this.writtenTextViewAdapter.disableOptions()
        }
    }

    private fun updateOptions(){
        when {
            this.dataList[0].LessonFormat == "SingleSelect" -> {
                this.singleSelectViewAdapter.updateOptions()
            }
            this.dataList[0].LessonFormat == "MultiSelect" -> {
                this.buildSentenceViewAdapter.updateOptions()
            }
            this.currentQuestion.LessonFormat == "ImageSelect" -> {
                this.imgChoiceViewAdapter.updateOptions()
            }
            else -> {
                this.writtenTextViewAdapter.updateOptions()
            }
        }
    }

    private fun setProgressBarStatus(){
        val percent: Double = (this.dataListSize - this.dataList.size).toDouble() / this.dataListSize.toDouble() * 100
        var result = percent.roundToInt()
        result = if(result == 0) 5 else result
        this.lessonStatusProgressBar!!.progress = result
    }

    private fun finishQuiz(){
        this.popUpWindow!!.dismiss()
        updateCompletedLesson()
    }

    private fun updateCompletedLesson(){
        val auth = FirebaseAuth.getInstance()
        val dbReference: DatabaseReference  = FirebaseDatabase.getInstance().reference

        val wordsLearned = max((lessonCount * 8), lastUpdatedWL)
        dbReference.child("Users").child(auth.currentUser!!.uid)
                .child("WordsLearned").setValue(wordsLearned.toString())
        val lessonsCompleted = max(lessonCount, lastUpdatedLC)
        dbReference.child("Users").child(auth.currentUser!!.uid)
                .child("LessonsCompleted").setValue(lessonsCompleted.toString())
        launchCompletedLessonScreen()
    }

    private fun launchCompletedLessonScreen(){
        val fm= supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        val lf = LessonCompletedFragment()
        ft.add(android.R.id.content, lf)
        ft.commit()
    }

    private fun isCorrectAnswer(): Boolean{
        return when {
            this.currentQuestion.LessonFormat == "MultiSelect" -> {
                buildSentenceViewAdapter.isCorrectAnswer()
            }
            this.currentQuestion.LessonFormat == "ImageSelect" -> imgChoiceViewAdapter.isCorrectAnswer()
            this.currentQuestion.LessonFormat in listOf("SingleSelect", "WrittenText") -> {
                this.currentQuestion.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                        this.selectedAnswer.toLowerCase(Locale.getDefault()).trim()
            }
            else -> false
        }
    }

    private fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button!!.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button!!.text = getString(buttonText)
        this.buttonState = buttonState
    }
}
