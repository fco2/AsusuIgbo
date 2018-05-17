package com.asusuigbo.frank.asusuigbo

import android.annotation.SuppressLint
import android.app.Activity
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
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.Option
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.QuestionsInfoAdapter
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.SharedData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class LessonActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var dataList: ArrayList<QuestionGroup> = ArrayList()
    var button: Button? = null
    var question: TextView? = null
    var popUpWindow: PopupWindow? = null
    var lessonsLayout: RelativeLayout? = null
    var myQueue: Queue<QuestionGroup> = LinkedList()
    var currentQuestionGroup: QuestionGroup? = null
    var activity: Activity = this
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var dbReference = database.getReference("Lessons/QuestionGroup")!!
    var lastQueItemPoll: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        this.lessonsLayout = findViewById(R.id.lessons_layout_id)
        this.button = findViewById(R.id.check_answer_button_id)
        this.question = findViewById(R.id.question_id)
        recyclerView = findViewById(R.id.question_recycler_view_id)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        this.populateList() //OR this.dataList = DummyList.getList()
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
            SharedData.CurrentListIndex < (this.dataList.size) -> {
                this.setUpButtonStateAndText(R.string.next_question_button_state, R.string.next_question_text)
                this.currentQuestionGroup = this.dataList[SharedData.CurrentListIndex]

            }
            (this.myQueue.isNotEmpty() && (SharedData.CurrentListIndex == ( this.dataList.size)) || this.lastQueItemPoll) -> {
                this.setUpButtonStateAndText(R.string.next_question_button_state, R.string.next_question_text)
            }
            else -> {
                this.button!!.isEnabled = true
                this.setUpButtonStateAndText(R.string.finished_button_state, R.string.finished_button_state)
            }
        }
        this.showPopUp()
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun showPopUp(){
        var layoutInflater: LayoutInflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var customView = layoutInflater.inflate(R.layout.popup_layout, null)
        this.popUpWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, 260)
        this.popUpWindow!!.elevation = 10.0f

        val textForView = customView.findViewById<TextView>(R.id.popup_text_result_id)
        if(this.isCorrectAnswer()){
            textForView.text = getString(R.string.you_are_correct_text)
            this.lastQueItemPoll = false //reset condition for last queue item
        }else{
            textForView.text = getString(R.string.sorry_wrong_answer_text)
            val rv = customView.findViewById<RelativeLayout>(R.id.custom_view_id)
            rv.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.wrongAnswer))
            val correctAnswerText = customView.findViewById<TextView>(R.id.correct_answer_id)
            correctAnswerText!!.text = getString(R.string.answer_template) + " " +
                    currentQuestionGroup!!.Options[currentQuestionGroup!!.CorrectAnswer.toInt()].OptionText

            this.myQueue.offer(this.currentQuestionGroup) //setup next question

            if(this.lastQueItemPoll){ //this ensures the last item is checked if its wrong
                this.setUpButtonStateAndText(R.string.next_question_button_state, R.string.next_question_text)
            }
        }

        val closeBtn = customView.findViewById<ImageButton>(R.id.close_popup_id)
        closeBtn.setOnClickListener{
            this.popUpWindow!!.dismiss()
        }
        this.popUpWindow!!.showAtLocation(this.lessonsLayout, Gravity.CENTER_HORIZONTAL, 0, 250)
    }

    private fun nextQuestion(){
        this.popUpWindow!!.dismiss()

        when {
            SharedData.CurrentListIndex < (this.dataList.size) -> { // Index protector, there is no next for array last index
                this.button!!.isEnabled = false
                SharedData.CurrentListIndex++ // Increment index

                when {
                    SharedData.CurrentListIndex < this.dataList.size -> {
                        this.currentQuestionGroup = this.dataList[SharedData.CurrentListIndex]
                        this.updateOptions()
                        this.setUpButtonStateAndText(R.string.answer_button_state, R.string.answer_button_state)
                    }
                    this.myQueue.isNotEmpty() && (SharedData.CurrentListIndex == ( this.dataList.size)) -> {
                        this.currentQuestionGroup = this.myQueue.poll()

                        if(this.myQueue.isEmpty())
                            this.lastQueItemPoll = true

                        this.setUpQueueListOptions()
                    }
                    else -> {
                        this.button!!.isEnabled = true
                        this.setUpButtonStateAndText(R.string.finished_button_state, R.string.finished_button_state)
                    }
                }
            }
            (this.myQueue.isNotEmpty() && (SharedData.CurrentListIndex == ( this.dataList.size)) || this.lastQueItemPoll) -> {
                this.currentQuestionGroup = this.myQueue.poll()

                if(this.myQueue.isEmpty())
                    this.lastQueItemPoll = true

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
        //Reset button state and list index
        SharedData.ButtonState =  getString(R.string.answer_button_state)
        SharedData.CurrentListIndex = 0
        this.button!!.text = getString(R.string.finished_button_state)
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun isCorrectAnswer(): Boolean{
        return  this.currentQuestionGroup!!.CorrectAnswer == SharedData.SelectedAnswerIndex.toString()
    }

    private fun setUpQueueListOptions(){
        this.updateOptions()
        this.button!!.isEnabled = false
        this.setUpButtonStateAndText(R.string.answer_button_state, R.string.answer_button_state)
    }

    private fun setUpButtonStateAndText(buttonState:Int, buttonText: Int){
        if(SharedData.CurrentListIndex >= this.dataList.size-1 && buttonState == R.string.next_question_button_state
                && this.myQueue.isEmpty()){
            this.button!!.text = getString(R.string.finished_button_state)
            SharedData.ButtonState = getString(R.string.finished_button_state)
        }else{
            this.button!!.text = getString(buttonText)
            SharedData.ButtonState = getString(buttonState)
        }
    }

    private fun populateList(){

        this.dbReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children){
                    var temp = QuestionGroup()
                    var optionsList = ArrayList<Option>()

                    temp.CorrectAnswer = d.child("CorrectAnswer").value.toString()
                    temp.Question = d.child("Question").value.toString()
                    temp.SelectedAnswer = d.child("SelectedAnswer").value.toString()

                    for(t in d.child("Options").children){
                        var text = Option(t.value.toString())
                        optionsList.add(text)
                    }
                    temp.Options = optionsList

                    dataList.add(temp)
                }
                //TODO: remove this
                question!!.text = dataList[0].Question
                SharedData.CorrectAnswerIndex = dataList[0].CorrectAnswer.toInt()
                recyclerView!!.adapter = QuestionsInfoAdapter(dataList[SharedData.CurrentListIndex].Options,
                       activity)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //do nothing
            }
        })
    }
}
