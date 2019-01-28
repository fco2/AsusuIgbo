package com.asusuigbo.frank.asusuigbo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.adapters.QuestionGroupAdapter
import com.asusuigbo.frank.asusuigbo.models.SharedData
import com.asusuigbo.frank.asusuigbo.models.UserButton

class LessonActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var dataList: ArrayList<QuestionGroup> = ArrayList()
    var button: Button? = null
    var question: TextView? = null
    private var popUpWindow: PopupWindow? = null
    private var lessonsLayout: RelativeLayout? = null
    private var currentQuestionGroup: QuestionGroup? = null
    private var context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        this.lessonsLayout = findViewById(R.id.lessons_layout_id)
        this.button = findViewById(R.id.check_answer_button_id)
        this.question = findViewById(R.id.question_id)
        recyclerView = findViewById(R.id.question_recycler_view_id)

        UserConnection.populateList(dataList, context, question!!, this, recyclerView!!)
        this.button!!.setOnClickListener(buttonClickListener)
    }

    private val buttonClickListener = View.OnClickListener {
        when(SharedData.ButtonState){
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
        this.currentQuestionGroup = this.dataList.removeAt(0)
        this.currentQuestionGroup!!.SelectedAnswer = SharedData.SelectedAnswerIndex.toString()
        this.displaySelectionInPopUp()

        //set selected answer back to -1.
        SharedData.SelectedAnswerIndex = -1
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
            var rv = customView.findViewById<RelativeLayout>(R.id.custom_view_id)
            var correctAnswerText = customView.findViewById<TextView>(R.id.correct_answer_id)
            rv.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.wrongAnswer))
            var msg = String.format("%s %s", getString(R.string.answer_template),
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
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = QuestionGroupAdapter(this.dataList[0].Options, this)
        this.recyclerView!!.adapter.notifyDataSetChanged() // this updates list
        this.question!!.text = this.dataList[0].Question
    }

    private fun finishQuiz(){
        //TODO: add more logic here
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun isCorrectAnswer(): Boolean{
        return  this.currentQuestionGroup!!.CorrectAnswer == this.currentQuestionGroup!!.SelectedAnswer
    }

    private fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button!!.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button!!.text = getString(buttonText)
        SharedData.ButtonState = buttonState
    }
}
