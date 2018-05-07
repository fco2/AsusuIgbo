package com.asusuigbo.frank.asusuigbo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.*

class LessonActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var dataList: ArrayList<QuestionGroup> = ArrayList()
    var button: Button? = null
    var question: TextView? = null
    var popUpWindow: PopupWindow? = null
    var lessonsLayout: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        this.lessonsLayout = findViewById(R.id.lessons_layout_id)
        this.button = findViewById(R.id.check_answer_button_id)
        this.question = findViewById(R.id.question_id)
        recyclerView = findViewById(R.id.question_recycler_view_id)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        //temp action
        this.dataList = DummyList.getList()
        //set question text
        this.question!!.text = this.dataList[0].Question

        recyclerView!!.adapter = QuestionsInfoAdapter(this.dataList[SharedData.CurrentListIndex].Options, this)
        this.button!!.setOnClickListener(buttonClickListener)

    }

    private val buttonClickListener = View.OnClickListener {
        when(SharedData.ButtonState){
            getString(R.string.answer_button_state) -> {
                this.answerQuestion()
            }
            getString(R.string.next_question_button_state) -> {
                this.nextQuestion()
            }
            getString(R.string.finished_button_state) -> {
                this.finishQuiz()
            }
            else -> {
                //do Nothing
            }
        }
        //TODO: At this point, we want to preserve 3 states, answer question, next question, finished.

        /* Toast.makeText(applicationContext, "Selected: ${SharedData.SelectedAnswerIndex}",
                Toast.LENGTH_LONG).show() */
    }

    private fun updateOptions(){
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        val nextQuestionGroup = this.dataList[SharedData.CurrentListIndex]
        recyclerView!!.adapter = QuestionsInfoAdapter(nextQuestionGroup.Options, this)
        this.recyclerView!!.adapter.notifyDataSetChanged() // this updates list
        this.question!!.text = nextQuestionGroup.Question
    }

    private fun answerQuestion(){
        //change button text
        //TODO: Logic to answer question

        if(SharedData.CurrentListIndex < ( this.dataList.size - 1)) {
            this.button!!.text = getString(R.string.next_question_text)
            SharedData.ButtonState = getString(R.string.next_question_button_state)
        }else{
            this.button!!.text = getString(R.string.finished_button_state)
            this.button!!.isEnabled = true
            SharedData.ButtonState = getString(R.string.finished_button_state)
        }
        this.showPopUp()
    }

    private fun nextQuestion(){
        if(SharedData.CurrentListIndex < ( this.dataList.size - 1)) { // Index protector
            this.button!!.isEnabled = false
            SharedData.CurrentListIndex = SharedData.CurrentListIndex + 1 //maybe this should be done in answer question
            this.updateOptions()
            this.button!!.text = getString(R.string.answer_button_state)
            SharedData.ButtonState = getString(R.string.answer_button_state)
        }
        else {
            this.button!!.text = getString(R.string.finished_button_state)
            this.button!!.isEnabled = true
            SharedData.ButtonState = getString(R.string.finished_button_state)
        }
    }

    @SuppressLint("InflateParams")
    private fun showPopUp(){
        //TODO: Check here if selected answer was correct
        var layoutInflater: LayoutInflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var customView = layoutInflater.inflate(R.layout.popup_layout, null)
        this.popUpWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, 200)
        this.popUpWindow!!.elevation = 10.0f

        //TODO: Set text on popup
        val textForView = customView.findViewById<TextView>(R.id.popup_text_result_id)
        if(this.IsCorrectAnswer()){
            textForView.text = getString(R.string.you_are_correct_text)
        }else{
            textForView.text = getString(R.string.sorry_wrong_answer_text)
        }

        val closeBtn = customView.findViewById<ImageButton>(R.id.close_popup_id)
        closeBtn.setOnClickListener{
            this.popUpWindow!!.dismiss()
        }
        this.popUpWindow!!.showAtLocation(this.lessonsLayout, Gravity.CENTER_HORIZONTAL, 0, 250)
    }

    private fun finishQuiz(){
        //TODO: Line below not needed, but it should launch finished activity.
        this.button!!.text = getString(R.string.finished_button_state)
    }

    private fun IsCorrectAnswer(): Boolean{
        return this.dataList[SharedData.CurrentListIndex].CorrectAnswer == SharedData.SelectedAnswerIndex
    }
}
