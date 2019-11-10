package com.asusuigbo.frank.asusuigbo.view.helpers

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.UserButton

class WrittenTextView(private val lessonActivity: LessonActivity) {
    var writtenTextLayout: RelativeLayout = lessonActivity.activity.findViewById(R.id.written_text_layout_id)
    var writtenTextQuestion: TextView = lessonActivity.activity.findViewById(R.id.written_text_question_id)
    private var writtenTextAnswer: EditText = lessonActivity.activity.findViewById(R.id.written_text_answer_id)

    init{
        this.writtenTextChangeListener()
    }

    fun updateOptions(){
        lessonActivity.viewDisplayManager("WrittenText")
        lessonActivity.selectedAnswer = ""
        this.writtenTextQuestion.text = lessonActivity.dataList[0].Question
        this.writtenTextAnswer.text.clear()
        this.writtenTextAnswer.isEnabled = true
    }

    fun disableOptions(){
        this.writtenTextAnswer.isEnabled = false
    }

    private fun writtenTextChangeListener(){
        this.writtenTextAnswer.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                lessonActivity.selectedAnswer = p0.toString()
                lessonActivity.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}