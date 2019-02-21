package com.asusuigbo.frank.asusuigbo

import android.app.FragmentManager
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.asusuigbo.frank.asusuigbo.connection.helpers.PopupHelper
import com.asusuigbo.frank.asusuigbo.connection.helpers.UserConnection
import com.asusuigbo.frank.asusuigbo.fragments.LessonCompletedFragment
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LessonActivity : AppCompatActivity() {

    private var dataList: ArrayList<QuestionGroup> = ArrayList()
    private var button: Button? = null
    private var question: TextView? = null
    private var popUpWindow: PopupWindow? = null
    private var lessonsLayout: RelativeLayout? = null
    private var currentQuestionGroup: QuestionGroup? = null
    private var requestedLesson: String = ""
    private var nextLesson: String = ""
    private var radioGroup: RadioGroup? = null
    private var optionA: RadioButton? = null
    private var optionB: RadioButton? = null
    private var optionC: RadioButton? = null
    private var optionD: RadioButton? = null
    private var buttonState: UserButton = UserButton.AnswerNotSelected
    private var progressBar: ProgressBar? = null
    private var lessonStatusProgressBar: ProgressBar? = null
    private var fullListSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        this.lessonsLayout = findViewById(R.id.lessons_layout_id)
        this.button = findViewById(R.id.check_answer_button_id)
        this.question = findViewById(R.id.question_id)
        this.radioGroup = findViewById(R.id.radio_group_id)
        this.optionA = radioGroup!!.findViewById(R.id.option_a_id)
        this.optionB = radioGroup!!.findViewById(R.id.option_b_id)
        this.optionC = radioGroup!!.findViewById(R.id.option_c_id)
        this.optionD = radioGroup!!.findViewById(R.id.option_d_id)
        this.progressBar = findViewById(R.id.progress_bar_lesson_id)
        this.lessonStatusProgressBar = findViewById(R.id.lesson_progress_id)

        this.progressBar!!.visibility = View.VISIBLE
        this.setLessonData()

        UserConnection.populateList(dataList, requestedLesson, question!!, radioGroup!!, this.progressBar!!)
        this.radioGroup!!.setOnCheckedChangeListener(radioGroupListener)
        this.button!!.setOnClickListener(buttonClickListener)
    }

    //TODO: take this
    private fun setLessonData(){
        this.requestedLesson = intent.getStringExtra("LESSON_NAME")
        this.nextLesson = intent.getStringExtra("NEXT_LESSON")
    }

    private val radioGroupListener = RadioGroup.OnCheckedChangeListener{ group, checkedId ->
        if(group.checkedRadioButtonId != -1){
            val checkedRadioBtn: RadioButton = findViewById(checkedId)
            this.dataList[0].SelectedAnswer = checkedRadioBtn.tag.toString()
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
        disableOptions()
        if(fullListSize == 0)
            fullListSize = dataList.size
        this.currentQuestionGroup = this.dataList.removeAt(0)
        this.popUpWindow = PopupHelper.displaySelectionInPopUp(this, lessonsLayout!!,
                currentQuestionGroup!!.Options[currentQuestionGroup!!.CorrectAnswer.toInt()], isCorrectAnswer())

        if(!this.isCorrectAnswer())
            this.dataList.add(this.currentQuestionGroup!!)

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
        this.question!!.text = this.dataList[0].Question
        optionA!!.text = dataList[0].Options[0]
        optionB!!.text = dataList[0].Options[1]
        optionC!!.text = dataList[0].Options[2]
        optionD!!.text = dataList[0].Options[3]
        enableOptions()
    }

    //TODO: take this
    private fun setProgressBarStatus()
    {
        val percent: Int = (fullListSize - this.dataList.size) * 10
        this.lessonStatusProgressBar!!.progress = percent
    }

    private fun finishQuiz(){
        this.popUpWindow!!.dismiss()
        updateCompletedLesson()
        launchCompletedLessonScreen()
    }

    private fun updateCompletedLesson(){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val dbReference: DatabaseReference = database.getReference("TableOfContent/Items")
        dbReference.child(this.nextLesson).child("LessonComplete").setValue("TRUE")
    }

    private fun launchCompletedLessonScreen(){
        val fm: FragmentManager = fragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.add(android.R.id.content, LessonCompletedFragment())
        ft.commit()
    }

    private fun isCorrectAnswer(): Boolean{
        return this.currentQuestionGroup!!.CorrectAnswer == this.currentQuestionGroup!!.SelectedAnswer
    }

    private fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button!!.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button!!.text = getString(buttonText)
        this.buttonState = buttonState
    }

    private fun disableOptions(){
        toggleOptionsFor(false)
    }

    private fun enableOptions(){
        toggleOptionsFor(true)
        radioGroup!!.clearCheck()
    }

    private fun toggleOptionsFor(state: Boolean){
        this.optionA!!.isEnabled = state
        this.optionB!!.isEnabled = state
        this.optionC!!.isEnabled = state
        this.optionD!!.isEnabled = state
    }
}
