package com.asusuigbo.frank.asusuigbo

import android.app.Activity
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.asusuigbo.frank.asusuigbo.connection.helpers.PopupHelper
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.fragments.LessonCompletedFragment
import com.asusuigbo.frank.asusuigbo.interfaces.ILesson
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LessonActivity : AppCompatActivity(), ILesson {
    override var dataListSize: Int = 0
    override var textViewClickListener = View.OnClickListener{}
    var lessonsLayout: RelativeLayout? = null
    private var radioGroup: RadioGroup? = null
    private lateinit var multiSelectLayout: RelativeLayout
    private lateinit var singleSelectLayout: RelativeLayout
    private var sourceFlexBoxLayout: FlexboxLayout? = null
    private var destFlexBoxLayout: FlexboxLayout? = null
    private var selectedSentence: ArrayList<Int> = ArrayList()
    
    override var dataList: ArrayList<QuestionGroup> = ArrayList()
    private var button: Button? = null
    private lateinit var singleQuestionTextView: TextView
    private lateinit var multiQuestionTextView: TextView
    private var popUpWindow: PopupWindow? = null
    lateinit var currentQuestion: QuestionGroup
    override var requestedLesson: String = ""
    private var nextLesson: String = ""
    private var buttonState: UserButton = UserButton.AnswerNotSelected
    override var progressBar: ProgressBar? = null
    private var lessonStatusProgressBar: ProgressBar? = null
    private var selectedAnswer = ""
    override var activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        this.lessonsLayout = findViewById(R.id.lessons_layout_id)
        this.button = findViewById(R.id.check_answer_button_id)
        this.singleQuestionTextView = findViewById(R.id.single_question_id)
        this.multiQuestionTextView = findViewById(R.id.multi_question_id)
        this.radioGroup = findViewById(R.id.radio_group_id)
        this.progressBar = findViewById(R.id.progress_bar_lesson_id)
        this.lessonStatusProgressBar = findViewById(R.id.lesson_progress_id)
        this.singleSelectLayout = findViewById(R.id.single_select_layout_id)
        this.multiSelectLayout = findViewById(R.id.multi_select_layout_id)
        sourceFlexBoxLayout = findViewById(R.id.flexbox_source_id)
        destFlexBoxLayout = findViewById(R.id.flexbox_destination_id)

        this.progressBar!!.visibility = View.VISIBLE
        this.setLessonData()

        DataLoader.populateList(this)
        this.radioGroup!!.setOnCheckedChangeListener(radioGroupListener)
        this.button!!.setOnClickListener(buttonClickListener)
    }

    init{
        textViewClickListener = View.OnClickListener { v ->
            if(!button!!.isEnabled)
                button!!.isEnabled = true
            this.buttonState = UserButton.AnswerSelected

            if(destFlexBoxLayout!!.indexOfChild(v) == -1){
                sourceFlexBoxLayout!!.removeView(v)
                destFlexBoxLayout!!.addView(v)
                selectedSentence.add(v.tag as Int)
            }else{
                destFlexBoxLayout!!.removeView(v)
                sourceFlexBoxLayout!!.addView(v)
                selectedSentence.remove(v.tag as Int)
            }
        }
    }

    private fun setLessonData(){
        this.requestedLesson = intent.getStringExtra("LESSON_NAME")
        this.nextLesson = intent.getStringExtra("NEXT_LESSON")
    }

    private val radioGroupListener = RadioGroup.OnCheckedChangeListener{ group, checkedId ->
        if(group.checkedRadioButtonId != -1){
            val checkedRadioBtn: RadioButton = findViewById(checkedId)
            this.selectedAnswer = checkedRadioBtn.text.toString()
            this.buttonState = UserButton.AnswerSelected
            this.button!!.isEnabled = true
        }
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
        if(this.dataList[0].LessonFormat == "SingleSelect"){
            multiSelectLayout.visibility = View.GONE
            singleSelectLayout.visibility = View.VISIBLE

            this.singleQuestionTextView.text = this.dataList[0].Question
            this.radioGroup!!.removeAllViews()
            this.selectedAnswer = ""
            DataLoader.buildRadioGroupContent(this)
        }else{
            singleSelectLayout.visibility = View.GONE
            multiSelectLayout.visibility = View.VISIBLE

            multiQuestionTextView.text = dataList[0].Question
            this.sourceFlexBoxLayout!!.removeAllViews()
            this.destFlexBoxLayout!!.removeAllViews()
            this.selectedSentence.clear()
            DataLoader.buildFlexBoxContent(this)
        }
    }

    private fun setProgressBarStatus()
    {
        val percent: Double = (this.dataListSize - this.dataList.size).toDouble() / this.dataListSize.toDouble() * 100
        var result = Math.round(percent).toInt()
        result = if(result == 0) 5 else result
        this.lessonStatusProgressBar!!.progress = result
    }

    private fun finishQuiz(){
        this.popUpWindow!!.dismiss()
        updateCompletedLesson()
        launchCompletedLessonScreen()
    }

    private fun updateCompletedLesson(){
        val auth = FirebaseAuth.getInstance()
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val dbReference: DatabaseReference = database.reference
        dbReference.child("UserLessonsActivated").child(auth.currentUser!!.uid)
                .child(this.nextLesson).setValue("TRUE")
    }

    private fun launchCompletedLessonScreen(){
        val fm: FragmentManager = fragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.add(android.R.id.content, LessonCompletedFragment())
        ft.commit()
    }

    override fun isCorrectAnswer(): Boolean{
        return if(this.currentQuestion.LessonFormat == "SingleSelect")
            this.currentQuestion.CorrectAnswer == this.selectedAnswer
        else{
            val sentence = this.buildSentence()
            sentence == currentQuestion.CorrectAnswer
        }
    }

    private fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button!!.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button!!.text = getString(buttonText)
        this.buttonState = buttonState
    }

    private fun disableOptions(){
        if(this.currentQuestion.LessonFormat == "SingleSelect"){
            for(index in 0 until radioGroup!!.childCount){
                val v = radioGroup!!.getChildAt(index)
                v.isEnabled = false
            }
        }else{
            disableViewsFor(sourceFlexBoxLayout!!)
            disableViewsFor(destFlexBoxLayout!!)
        }
    }

    private fun disableViewsFor(flyt: FlexboxLayout){
        for(index in 0 until flyt.childCount){
            val v = flyt.getChildAt(index)
            v.isEnabled = false
        }
    }

    private fun buildSentence(): String{
        val sb = StringBuilder()
        for(item in selectedSentence){
            sb.append(currentQuestion.Options.elementAt(item)).append(" ")
        }
        return sb.toString().trim()
    }
}
