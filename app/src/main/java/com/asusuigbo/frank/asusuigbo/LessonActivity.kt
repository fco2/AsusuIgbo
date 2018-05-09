package com.asusuigbo.frank.asusuigbo

import android.annotation.SuppressLint
import android.content.Context
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
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.DummyList
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.QuestionsInfoAdapter
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.SharedData
import java.util.*
import kotlin.collections.ArrayList

class LessonActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var dataList: ArrayList<QuestionGroup> = ArrayList()
    var button: Button? = null
    var question: TextView? = null
    var popUpWindow: PopupWindow? = null
    var lessonsLayout: RelativeLayout? = null
    var myQueue: Queue<QuestionGroup> = LinkedList() //this.myQueue.addAll(this.dataList)
    var currentQuestionGroup: QuestionGroup? = null

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
        SharedData.CorrectAnswerIndex = this.dataList[0].CorrectAnswer

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
        }
    }

    private fun answerQuestion(){
        when {
            SharedData.CurrentListIndex != (this.dataList.size) -> {
                this.setUpButtonStateAndText(R.string.next_question_button_state, R.string.next_question_text)
                this.currentQuestionGroup = this.dataList[SharedData.CurrentListIndex] //TODO: check for bug --> set currentgroup
                SharedData.CorrectAnswerIndex = this.currentQuestionGroup!!.CorrectAnswer
            }
            this.myQueue.isNotEmpty() && (SharedData.CurrentListIndex == ( this.dataList.size)) -> {
                this.setUpButtonStateAndText(R.string.next_question_button_state, R.string.next_question_text)
            }
            else -> {
                this.button!!.isEnabled = true
                this.setUpButtonStateAndText(R.string.finished_button_state, R.string.finished_button_state)
            }
        }
        this.showPopUp()
    }

    @SuppressLint("InflateParams")
    private fun showPopUp(){
        var layoutInflater: LayoutInflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var customView = layoutInflater.inflate(R.layout.popup_layout, null)
        this.popUpWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, 200)
        this.popUpWindow!!.elevation = 10.0f

        val textForView = customView.findViewById<TextView>(R.id.popup_text_result_id)
        if(this.isCorrectAnswer()){
            textForView.text = getString(R.string.you_are_correct_text)
        }else{
            textForView.text = getString(R.string.sorry_wrong_answer_text)
            val rv = customView.findViewById<RelativeLayout>(R.id.custom_view_id)
            rv.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.wrongAnswer))
            //setup next question
            this.myQueue.offer(this.currentQuestionGroup)
        }

        val closeBtn = customView.findViewById<ImageButton>(R.id.close_popup_id)
        closeBtn.setOnClickListener{
            this.popUpWindow!!.dismiss()
        }
        this.popUpWindow!!.showAtLocation(this.lessonsLayout, Gravity.CENTER_HORIZONTAL, 0, 250)
    }

    private fun nextQuestion(){
        when {
            SharedData.CurrentListIndex < (this.dataList.size) -> { // Index protector, there is no next for array last index
                this.button!!.isEnabled = false

                SharedData.CurrentListIndex++

                if( SharedData.CurrentListIndex < this.dataList.size){
                    this.currentQuestionGroup = this.dataList[SharedData.CurrentListIndex]
                    this.updateOptions()
                    this.setUpButtonStateAndText(R.string.answer_button_state, R.string.answer_button_state)
                }else if(this.myQueue.isNotEmpty() && (SharedData.CurrentListIndex == ( this.dataList.size))) {
                    this.currentQuestionGroup = this.myQueue.poll()
                    SharedData.CorrectAnswerIndex = this.currentQuestionGroup!!.CorrectAnswer
                    this.setUpQueueListOptions()
                }else{
                    this.button!!.isEnabled = true
                    this.setUpButtonStateAndText(R.string.finished_button_state, R.string.finished_button_state)
                }
            }
            this.myQueue.isNotEmpty() && (SharedData.CurrentListIndex == ( this.dataList.size)) -> {
                //TODO: make logic better
                this.currentQuestionGroup = this.myQueue.poll()
                SharedData.CorrectAnswerIndex = this.currentQuestionGroup!!.CorrectAnswer
                this.setUpQueueListOptions()
            }
            else -> {
                this.button!!.isEnabled = true
                this.setUpButtonStateAndText(R.string.finished_button_state, R.string.finished_button_state)
            }
        }
    }

    private fun updateOptions(){
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = QuestionsInfoAdapter(this.currentQuestionGroup!!.Options, this)
        this.recyclerView!!.adapter.notifyDataSetChanged() // this updates list
        this.question!!.text = this.currentQuestionGroup!!.Question
    }

    private fun finishQuiz(){
        //TODO: Line below not needed, but it should launch finished activity.
        this.button!!.text = getString(R.string.finished_button_state)

        //TODO: remove immediately
        Toast.makeText(applicationContext, "Failed: ${this.myQueue.size}",
                Toast.LENGTH_LONG).show()
    }

    private fun isCorrectAnswer(): Boolean{
        return  SharedData.CorrectAnswerIndex == SharedData.SelectedAnswerIndex
    }

    private fun setUpQueueListOptions(){
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = QuestionsInfoAdapter(this.currentQuestionGroup!!.Options, this)
        this.recyclerView!!.adapter.notifyDataSetChanged() // this updates list
        this.question!!.text = this.currentQuestionGroup!!.Question

        this.button!!.isEnabled = false
        this.setUpButtonStateAndText(R.string.answer_button_state, R.string.answer_button_state)
    }

    private fun setUpButtonStateAndText(buttonState:Int, buttonText: Int){
        this.button!!.text = getString(buttonText)
        SharedData.ButtonState = getString(buttonState)
    }
}
