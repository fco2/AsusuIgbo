package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.LessonActivity

import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.TestDictActivity
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.models.UserButton

/**
 * A simple [Fragment] subclass.
 */
class SingleSelectFragment(var lessonActivity: LessonActivity) : Fragment() {

    private lateinit var button: Button
    private var radioGroup: RadioGroup = lessonActivity.findViewById(R.id.radio_group_id)
    var singleQuestionTextView: TextView = lessonActivity.findViewById(R.id.single_question_id)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_select, container, false)
        button = view.findViewById(R.id.button_id)
        button.setOnClickListener(buttonClickListener)
        return view
    }

    companion object{
        fun getInstance(lessonActivity: LessonActivity): SingleSelectFragment{
            return SingleSelectFragment(lessonActivity)
        }
    }

    private val buttonClickListener = View.OnClickListener {
        //testDictActivity.toggleFragment("B")
    }

    init{
        this.invokeCheckedButtonListener()
    }

    private fun invokeCheckedButtonListener(){
        radioGroup.setOnCheckedChangeListener{ group, checkedId ->
            if(group.checkedRadioButtonId != -1){ //if it is a valid checked radio button
                val checkedRadioBtn: RadioButton = lessonActivity.findViewById(checkedId)
                lessonActivity.selectedAnswer = checkedRadioBtn.text.toString()
                lessonActivity.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
            }
        }
    }

    fun updateOptions(){
        //lessonActivity.viewDisplayManager()
        this.singleQuestionTextView.text = lessonActivity.dataList[0].Question
        this.radioGroup.removeAllViews()
        lessonActivity.selectedAnswer = ""
        DataLoader.buildRadioGroupContent(this)
    }

    fun disableOptions(){
        for(index in 0 until this.radioGroup.childCount){
            val v = this.radioGroup.getChildAt(index)
            v.isEnabled = false
        }
    }

}
