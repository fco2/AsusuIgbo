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
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.*
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

    //TODO: clean up
    var myQueueTempHolder: QuestionGroup? = null
    //myQueue.remove() //removes the first element entered
    //head = myQueue.poll();
    //could be easier to have another list of ints 0 or 1. 1 for answered.

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
            else -> {
                //do Nothing
            }
        }
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
        //TODO: Logic to answer question
        when {
            SharedData.CurrentListIndex < ( this.dataList.size - 1) -> {
                this.button!!.text = getString(R.string.next_question_text)
                SharedData.ButtonState = getString(R.string.next_question_button_state)

                SharedData.CorrectAnswerIndex = this.dataList[SharedData.CurrentListIndex].CorrectAnswer
            }
            this.myQueue.isNotEmpty() && (SharedData.CurrentListIndex == ( this.dataList.size - 1)) -> {
                this.button!!.text = getString(R.string.next_question_text)
                SharedData.ButtonState = getString(R.string.next_question_button_state)

                //TODO: make logic better
                this.myQueueTempHolder = this.myQueue.poll()
                SharedData.CorrectAnswerIndex = this.myQueueTempHolder!!.CorrectAnswer
            }
            else -> {
                //TODO: put logic here to check for if queue is not empty //consider using else/if here
                this.button!!.text = getString(R.string.finished_button_state)
                this.button!!.isEnabled = true
                SharedData.ButtonState = getString(R.string.finished_button_state)

            }
        }
        this.showPopUp()
    }

    private fun nextQuestion(){
        when {
            SharedData.CurrentListIndex < ( this.dataList.size - 1) -> { // Index protector
                this.button!!.isEnabled = false
                SharedData.CurrentListIndex = SharedData.CurrentListIndex + 1
                this.updateOptions()
                this.button!!.text = getString(R.string.answer_button_state)
                SharedData.ButtonState = getString(R.string.answer_button_state)
            }
            this.myQueue.isNotEmpty() && (SharedData.CurrentListIndex == ( this.dataList.size - 1)) -> {
                this.setUpQueueListOptions()
            }
            else -> {
                this.button!!.text = getString(R.string.finished_button_state)
                this.button!!.isEnabled = true
                SharedData.ButtonState = getString(R.string.finished_button_state)
            }
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
        if(this.isCorrectAnswer()){
            textForView.text = getString(R.string.you_are_correct_text)
        }else{
            textForView.text = getString(R.string.sorry_wrong_answer_text)
            val rv = customView.findViewById<RelativeLayout>(R.id.custom_view_id)
            rv.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.wrongAnswer))

            //TODO: testing
            this.myQueue.offer(this.dataList[SharedData.CurrentListIndex])
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

        //TODO: remove immediately
        Toast.makeText(applicationContext, "Failed: ${this.myQueue.size}",
                Toast.LENGTH_LONG).show()
    }

    private fun isCorrectAnswer(): Boolean{
        //TODO: someone has to keep setting correct answer || --> inconsistent --> SharedData.SelectedAnswerIndex
        return  SharedData.CorrectAnswerIndex == SharedData.SelectedAnswerIndex
    }

    private fun setUpQueueListOptions(){
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = QuestionsInfoAdapter(this.myQueueTempHolder!!.Options, this)
        this.recyclerView!!.adapter.notifyDataSetChanged() // this updates list
        this.question!!.text = this.myQueueTempHolder!!.Question

        this.button!!.isEnabled = false
        this.button!!.text = getString(R.string.answer_button_state)
        SharedData.ButtonState = getString(R.string.answer_button_state)
    }
}
