package com.asusuigbo.frank.asusuigbo.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.models.UserButton

class WrittenTextViewAdapter(private val lessonActivity: LessonActivity) {
    fun updateOptions(){
        lessonActivity.singleSelectLayout.visibility = View.GONE
        lessonActivity.multiSelectLayout.visibility = View.GONE
        lessonActivity.writtenTextLayout.visibility = View.VISIBLE
        lessonActivity.selectedAnswer = ""
        lessonActivity.writtenTextQuestion.text = lessonActivity.dataList[0].Question
        lessonActivity.writtenTextAnswer.text.clear()
        lessonActivity.writtenTextAnswer.isEnabled = true
    }

    fun disableOptions(){
        lessonActivity.writtenTextAnswer.isEnabled = false
    }

    fun writtenTextChangeListener(){
        lessonActivity.writtenTextAnswer.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                lessonActivity.selectedAnswer = p0.toString()
                lessonActivity.buttonState = UserButton.AnswerSelected
                lessonActivity.button!!.isEnabled = lessonActivity.selectedAnswer.trim().isNotEmpty()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}