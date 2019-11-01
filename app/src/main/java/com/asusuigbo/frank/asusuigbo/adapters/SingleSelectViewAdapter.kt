package com.asusuigbo.frank.asusuigbo.adapters

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.models.UserButton

class SingleSelectViewAdapter(private val lessonActivity: LessonActivity) {
    fun updateOptions(){
        lessonActivity.multiSelectLayout.visibility = View.GONE
        lessonActivity.writtenTextLayout.visibility = View.GONE
        lessonActivity.singleSelectLayout.visibility = View.VISIBLE

        lessonActivity.singleQuestionTextView.text = lessonActivity.dataList[0].Question
        lessonActivity.radioGroup!!.removeAllViews()
        lessonActivity.selectedAnswer = ""
        DataLoader.buildRadioGroupContent(lessonActivity)
    }

    fun disableOptions(){
        for(index in 0 until lessonActivity.radioGroup!!.childCount){
            val v = lessonActivity.radioGroup!!.getChildAt(index)
            v.isEnabled = false
        }
    }

    val radioGroupListener = RadioGroup.OnCheckedChangeListener{ group, checkedId ->
        if(group.checkedRadioButtonId != -1){
            val checkedRadioBtn: RadioButton = lessonActivity.activity.findViewById(checkedId)
            lessonActivity.selectedAnswer = checkedRadioBtn.text.toString()
            lessonActivity.buttonState = UserButton.AnswerSelected
            lessonActivity.button!!.isEnabled = true
        }
    }
}