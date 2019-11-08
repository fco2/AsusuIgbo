package com.asusuigbo.frank.asusuigbo.view.helpers

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.models.UserButton

class SingleSelectView(private val lessonActivity: LessonActivity) {
    private var radioGroup: RadioGroup = lessonActivity.activity.findViewById(R.id.radio_group_id)
    var singleQuestionTextView: TextView = lessonActivity.activity.findViewById(R.id.single_question_id)
    var singleSelectLayout: RelativeLayout = lessonActivity.activity.findViewById(R.id.single_select_layout_id)

    init{

        val radioGroupListener = RadioGroup.OnCheckedChangeListener{ group, checkedId ->
            if(group.checkedRadioButtonId != -1){ //if it is a valid checked radio button
                val checkedRadioBtn: RadioButton = lessonActivity.activity.findViewById(checkedId)
                lessonActivity.selectedAnswer = checkedRadioBtn.text.toString()
                lessonActivity.buttonState = UserButton.AnswerSelected
                lessonActivity.button!!.isEnabled = true
            }
        }
        this.radioGroup.setOnCheckedChangeListener(radioGroupListener)
    }

    fun updateOptions(){
        lessonActivity.buildSentenceView.multiSelectLayout.visibility = View.GONE
        lessonActivity.writtenTextView.writtenTextLayout.visibility = View.GONE
        lessonActivity.imgChoiceView.imgChoiceLayout.visibility = View.GONE
        lessonActivity.singleSelectView.singleSelectLayout.visibility = View.VISIBLE
        this.singleQuestionTextView.text = lessonActivity.dataList[0].Question
        this.radioGroup.removeAllViews()
        lessonActivity.selectedAnswer = ""
        DataLoader.buildRadioGroupContent(lessonActivity)
    }

    fun disableOptions(){
        for(index in 0 until this.radioGroup.childCount){
            val v = this.radioGroup.getChildAt(index)
            v.isEnabled = false
        }
    }
}