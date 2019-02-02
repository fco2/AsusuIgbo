package com.asusuigbo.frank.asusuigbo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.models.UserButton

class LessonActivity : AppCompatActivity() {

    private var dataList: ArrayList<QuestionGroup> = ArrayList()
    private var button: Button? = null
    private var question: TextView? = null
    private var popUpWindow: PopupWindow? = null
    private var lessonsLayout: RelativeLayout? = null
    private var currentQuestionGroup: QuestionGroup? = null
    private var requestedLesson: String = ""
    private var radioGroup: RadioGroup? = null
    private var optionA: RadioButton? = null
    private var optionB: RadioButton? = null
    private var optionC: RadioButton? = null
    private var optionD: RadioButton? = null
    private var buttonState: UserButton = UserButton.AnswerNotSelected

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

        this.setLessonName()
        UserConnection.populateList(dataList, requestedLesson, question!!, radioGroup!!)
        this.radioGroup!!.setOnCheckedChangeListener(radioGroupListener)
        this.button!!.setOnClickListener(buttonClickListener)
    }

    private fun setLessonName(){
        this.requestedLesson = intent.getStringExtra("LESSON_NAME")
    }

    private val radioGroupListener = RadioGroup.OnCheckedChangeListener{ group, checkedId ->
        if(group.checkedRadioButtonId != -1){
            val checkedRadioBtn: RadioButton = findViewById(checkedId)
            setSelectedBackground(checkedRadioBtn)
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
        this.currentQuestionGroup = this.dataList.removeAt(0)
        this.displaySelectionInPopUp()

        if(!this.isCorrectAnswer())
            this.dataList.add(this.currentQuestionGroup!!)

        if(this.dataList.size > 0)
            this.setUpButtonStateAndText(UserButton.NextQuestion, R.string.next_question_text)
        else
            this.setUpButtonStateAndText(UserButton.Finished, R.string.finished_button_state)
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun displaySelectionInPopUp(){
        val layoutInflater: LayoutInflater =
                baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = layoutInflater.inflate(R.layout.popup_layout, null)
        this.popUpWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, 260)
        this.popUpWindow!!.elevation = 10.0f
        val popUpTextResult = customView.findViewById<TextView>(R.id.popup_text_result_id)
        this.stylePopUp(popUpTextResult, customView)
        this.popUpWindow!!.showAtLocation(this.lessonsLayout, Gravity.CENTER_HORIZONTAL, 0, 250)
    }

    private fun stylePopUp(popUpTextResult: TextView, customView: View) {
        if(this.isCorrectAnswer()){
            popUpTextResult.text = getString(R.string.you_are_correct_text)
        }else{
            popUpTextResult.text = getString(R.string.sorry_wrong_answer_text)
            val rv = customView.findViewById<RelativeLayout>(R.id.custom_view_id)
            val correctAnswerText = customView.findViewById<TextView>(R.id.correct_answer_id)
            rv.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.wrongAnswer))
            val msg = String.format("%s %s", getString(R.string.answer_template),
                    currentQuestionGroup!!.Options[currentQuestionGroup!!.CorrectAnswer.toInt()])
            correctAnswerText!!.text = msg
        }
    }

    private fun nextQuestion(){
        this.popUpWindow!!.dismiss()
        this.updateOptions()
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
    }

    private fun updateOptions(){
        this.question!!.text = this.dataList[0].Question
        optionA!!.text = dataList[0].Options[0]
        optionB!!.text = dataList[0].Options[1]
        optionC!!.text = dataList[0].Options[2]
        optionD!!.text = dataList[0].Options[3]
        enableOptions()
        clearSelectedBackground()
    }

    private fun finishQuiz(){
        //TODO: add more logic here
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
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
        this.optionA!!.isEnabled = false
        this.optionB!!.isEnabled = false
        this.optionC!!.isEnabled = false
        this.optionD!!.isEnabled = false
    }

    private fun enableOptions(){
        this.optionA!!.isEnabled = true
        this.optionB!!.isEnabled = true
        this.optionC!!.isEnabled = true
        this.optionD!!.isEnabled = true
        radioGroup!!.clearCheck()
    }

    private fun setSelectedBackground(btn: RadioButton){
        clearSelectedBackground()
        btn.background = getDrawable(R.drawable.option_selected_background)
    }

    private fun clearSelectedBackground(){
        this.optionA!!.background = getDrawable(R.drawable.option_background)
        this.optionB!!.background = getDrawable(R.drawable.option_background)
        this.optionC!!.background = getDrawable(R.drawable.option_background)
        this.optionD!!.background = getDrawable(R.drawable.option_background)

    }
}
