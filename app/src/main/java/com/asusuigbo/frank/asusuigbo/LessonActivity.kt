package com.asusuigbo.frank.asusuigbo

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.asusuigbo.frank.asusuigbo.adapters.BuildSentenceViewAdapter
import com.asusuigbo.frank.asusuigbo.adapters.SingleSelectViewAdapter
import com.asusuigbo.frank.asusuigbo.adapters.WrittenTextViewAdapter
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.connection.helpers.PopupHelper
import com.asusuigbo.frank.asusuigbo.fragments.LessonCompletedFragment
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.android.flexbox.FlexboxLayout
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
     var textViewClickListener = View.OnClickListener{}
     var dataList: ArrayList<QuestionGroup> = ArrayList()
     var progressBar: ProgressBar? = null
     var requestedLesson: String = ""
     var activity: Activity = this
    var lessonsLayout: RelativeLayout? = null
    var radioGroup: RadioGroup? = null
    lateinit var multiSelectLayout: RelativeLayout
    lateinit var singleSelectLayout: RelativeLayout
    lateinit var writtenTextLayout: RelativeLayout
    lateinit var writtenTextQuestion: TextView
    lateinit var writtenTextAnswer: EditText
    var sourceFlexBoxLayout: FlexboxLayout? = null
    var destFlexBoxLayout: FlexboxLayout? = null
    var selectedSentence: ArrayList<Int> = ArrayList()
    var button: Button? = null
    lateinit var singleQuestionTextView: TextView
    lateinit var multiQuestionTextView: TextView
    private var popUpWindow: PopupWindow? = null
    lateinit var currentQuestion: QuestionGroup
    private var nextLesson: String = ""
    var buttonState: UserButton = UserButton.AnswerNotSelected
    private var lessonStatusProgressBar: ProgressBar? = null
    var selectedAnswer = ""
    private var lessonCount = 0

    private lateinit var singleSelectViewAdapter: SingleSelectViewAdapter
    private var  buildSentenceViewAdapter: BuildSentenceViewAdapter = BuildSentenceViewAdapter(this)
    private lateinit var writtenTextViewAdapter: WrittenTextViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        this.singleSelectViewAdapter = SingleSelectViewAdapter(this)
        this.writtenTextViewAdapter = WrittenTextViewAdapter(this)

        this.lessonsLayout = findViewById(R.id.lessons_layout_id)
        this.button = findViewById(R.id.check_answer_button_id)
        this.singleQuestionTextView = findViewById(R.id.single_question_id)
        this.multiQuestionTextView = findViewById(R.id.multi_question_id)
        this.radioGroup = findViewById(R.id.radio_group_id)
        this.progressBar = findViewById(R.id.progress_bar_lesson_id)
        this.lessonStatusProgressBar = findViewById(R.id.lesson_progress_id)
        this.singleSelectLayout = findViewById(R.id.single_select_layout_id)
        this.multiSelectLayout = findViewById(R.id.multi_select_layout_id)
        this.sourceFlexBoxLayout = findViewById(R.id.flexbox_source_id)
        this.destFlexBoxLayout = findViewById(R.id.flexbox_destination_id)
        this.writtenTextLayout = findViewById(R.id.written_text_layout_id)
        this.writtenTextQuestion = findViewById(R.id.written_text_question_id)
        this.writtenTextAnswer = findViewById(R.id.written_text_answer_id)

        this.progressBar!!.visibility = View.VISIBLE
        this.setLessonData()

        DataLoader.populateList(this)
        this.radioGroup!!.setOnCheckedChangeListener(this.singleSelectViewAdapter.radioGroupListener)
        this.button!!.setOnClickListener(buttonClickListener)
        this.writtenTextViewAdapter.writtenTextChangeListener()
    }

    init{
        this.buildSentenceViewAdapter.initializeViewClickListener()
    }

    private fun setLessonData(){
        this.requestedLesson = intent.getStringExtra("LESSON_NAME")
        this.nextLesson = intent.getStringExtra("NEXT_LESSON")
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

    private fun updateOptions(){
        when {
            this.dataList[0].LessonFormat == "SingleSelect" -> {
                this.singleSelectViewAdapter.updateOptions()
            }
            this.dataList[0].LessonFormat == "MultiSelect" -> {
                this.buildSentenceViewAdapter.updateOptions()
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
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val dbReference: DatabaseReference = database.reference

        dbReference.child("UserLessonsActivated").child(auth.currentUser!!.uid)
                .child(this.nextLesson).setValue("TRUE")

        var temp = max((lessonCount * 8), lastUpdatedWL)
        dbReference.child("Users").child(auth.currentUser!!.uid)
                .child("WordsLearned").setValue(temp.toString())
        temp = max(lessonCount, lastUpdatedLC)
        dbReference.child("Users").child(auth.currentUser!!.uid)
                .child("LessonsCompleted").setValue(temp.toString())
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
        return if(this.currentQuestion.LessonFormat == "MultiSelect"){
            val sentence = this.buildSentenceViewAdapter.buildSentence()
            sentence == currentQuestion.CorrectAnswer
        }else{
            this.currentQuestion.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                    this.selectedAnswer.toLowerCase(Locale.getDefault()).trim()
        }
    }

    private fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button!!.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button!!.text = getString(buttonText)
        this.buttonState = buttonState
    }

    private fun disableOptions(){
        when {
            this.currentQuestion.LessonFormat == "SingleSelect" -> {
                this.singleSelectViewAdapter.disableOptions()
            }
            this.currentQuestion.LessonFormat == "MultiSelect" -> {
                this.buildSentenceViewAdapter.disableOptions()
            }
            else -> this.writtenTextViewAdapter.disableOptions()
        }
    }
}
