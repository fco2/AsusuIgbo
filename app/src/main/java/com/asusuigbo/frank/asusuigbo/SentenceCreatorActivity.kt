package com.asusuigbo.frank.asusuigbo

import android.app.FragmentManager
import android.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.asusuigbo.frank.asusuigbo.connection.helpers.PopupHelper
import com.asusuigbo.frank.asusuigbo.connection.helpers.SentenceCreatorHelper
import com.asusuigbo.frank.asusuigbo.fragments.LessonCompletedFragment
import com.asusuigbo.frank.asusuigbo.models.SentenceInfo
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SentenceCreatorActivity : AppCompatActivity() {

    var sourceFlexBoxLayout: FlexboxLayout? = null
    var destFlexBoxLayout: FlexboxLayout? = null
    //TODO: Refactor names to proper naming convention
    private var sentenceList: ArrayList<SentenceInfo> = ArrayList()
    private var workingList: ArrayList<SentenceInfo> = ArrayList()
    private lateinit var currentQuestion: SentenceInfo
    private var selectedSentence: ArrayList<Int> = ArrayList()
    private var button: Button? = null
    private var textView: TextView? = null
    private var buttonState: UserButton = UserButton.AnswerNotSelected
    private lateinit var profileLayout: RelativeLayout
    private var popUpWindow: PopupWindow? = null
    private var requestedLesson: String = ""
    private var nextLesson: String = ""
    private var progressBar: ProgressBar? = null
    private var lessonStatusProgressBar: ProgressBar? = null
    private var fullListSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sentence_creator)

        sourceFlexBoxLayout = findViewById(R.id.flexbox_source_id)
        destFlexBoxLayout = findViewById(R.id.flexbox_destination_id)
        button = findViewById(R.id.check_answer_button_id)
        textView = findViewById(R.id.profile_question_id)
        profileLayout = findViewById(R.id.profile_layout_id)
        this.progressBar = findViewById(R.id.progress_bar_lesson_id)
        this.lessonStatusProgressBar = findViewById(R.id.lesson_progress_id)
        this.setLessonData()

        SentenceCreatorHelper.populateList(sentenceList, workingList, this,
                textViewClickListener, this.requestedLesson)
        button!!.setOnClickListener(buttonClickListener)
    }

    private fun setLessonData(){
        this.requestedLesson = intent.getStringExtra("LESSON_NAME")
        this.nextLesson = intent.getStringExtra("NEXT_LESSON")
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

    private val textViewClickListener = View.OnClickListener { v ->
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

    private fun answerQuestion(){
        disableOptions()
        if(fullListSize == 0)
            fullListSize = workingList.size
        this.currentQuestion = this.workingList.removeAt(0)
        this.popUpWindow = PopupHelper.displaySelectionInPopUp(this, profileLayout,
                currentQuestion.correctAnswer, isCorrectAnswer())

        if(!this.isCorrectAnswer())
            this.workingList.add(this.currentQuestion)

        if(this.workingList.size > 0)
            this.setUpButtonStateAndText(UserButton.NextQuestion, R.string.next_question_text)
        else
            this.setUpButtonStateAndText(UserButton.Finished, R.string.continue_text)
    }

    private fun nextQuestion(){
        popUpWindow!!.dismiss()
        this.updateOptions()
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
        setProgressBarStatus()
    }

    private fun setProgressBarStatus()
    {
        val percent: Int = (fullListSize - this.workingList.size) * 10
        this.lessonStatusProgressBar!!.progress = percent
    }

    private fun updateOptions(){
        textView!!.text = workingList[0].fullSentence
        this.sourceFlexBoxLayout!!.removeAllViews()
        this.destFlexBoxLayout!!.removeAllViews()
        this.selectedSentence.clear()
        SentenceCreatorHelper.buildFlexBoxContent(workingList[0].wordBlocks, sourceFlexBoxLayout!!,
                this, textViewClickListener)
    }

    private fun finishQuiz(){
        popUpWindow!!.dismiss()
        updateCompletedLesson()
        launchCompletedLessonScreen()
    }

    private fun updateCompletedLesson(){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val dbReference: DatabaseReference = database.getReference("TableOfContent/Items")
        dbReference.child(this.nextLesson).child("LessonComplete").setValue("TRUE")
    }

    private fun disableOptions(){
        disableViewsFor(sourceFlexBoxLayout!!)
        disableViewsFor(destFlexBoxLayout!!)
    }

    private fun disableViewsFor(flyt: FlexboxLayout){
        for(index in 0 until flyt.childCount){
            val v = flyt.getChildAt(index)
            v.isEnabled = false
        }
    }

    private fun launchCompletedLessonScreen(){
        val fm: FragmentManager = fragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.add(android.R.id.content, LessonCompletedFragment())
        ft.commit()
    }

    private fun buildSentence(): String{
        val sb = StringBuilder()
        for(item in selectedSentence){
            sb.append(currentQuestion.wordBlocks.elementAt(item)).append(" ")
        }
        return sb.toString().trim()
    }

    private fun isCorrectAnswer(): Boolean{
        val sentence = this.buildSentence()
        return sentence == currentQuestion.correctAnswer
    }

    private fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button!!.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button!!.text = getString(buttonText)
        this.buttonState = buttonState
    }
}
