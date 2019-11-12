package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.UserButton

/**
 * A simple [Fragment] subclass.
 */
class SingleSelectFragment(var lessonActivity: LessonActivity) : Fragment() {

    private lateinit var button: Button
    private lateinit var radioGroup: RadioGroup
    var singleQuestionTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_single_select, container, false)
        button = view.findViewById(R.id.button_id)
        radioGroup = view.findViewById(R.id.radio_group_id)
        this.invokeCheckedButtonListener()
        singleQuestionTextView = view.findViewById(R.id.single_question_id)
        button.setOnClickListener(buttonClickListener)

        lessonActivity.singleSelectFragment.singleQuestionTextView!!.text =
            lessonActivity.dataList[0].Question
        this.buildRadioGroupContent()

        return view
    }

    companion object{
        fun getInstance(lessonActivity: LessonActivity): SingleSelectFragment{
            return SingleSelectFragment(lessonActivity)
        }
    }

    private val buttonClickListener = View.OnClickListener {
        lessonActivity.executeButtonAction()
    }

    private fun invokeCheckedButtonListener(){
        radioGroup.setOnCheckedChangeListener{ group, checkedId ->
            if(group.checkedRadioButtonId != -1){ //if it is a valid checked radio button
                val checkedRadioBtn: RadioButton = view!!.findViewById(checkedId)
                lessonActivity.selectedAnswer = checkedRadioBtn.text.toString()
                this.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
            }
        }
    }

    fun updateOptions(){
        lessonActivity.navigateToFragment("SingleSelect")
        this.singleQuestionTextView!!.text = lessonActivity.dataList[0].Question
        this.radioGroup.removeAllViews()
        lessonActivity.selectedAnswer = ""
        this.buildRadioGroupContent()
    }

    fun disableOptions(){
        for(index in 0 until this.radioGroup.childCount){
            val v = this.radioGroup.getChildAt(index)
            v.isEnabled = false
        }
    }

    fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button.text = getString(buttonText)
        lessonActivity.buttonState = buttonState
    }

    private fun buildRadioGroupContent(){
        val rg = this.view!!.findViewById<RadioGroup>(R.id.radio_group_id)
        rg.clearCheck()
        for((index,item) in this.lessonActivity.dataList[0].Options.withIndex()){
            val view = RadioButton(this.lessonActivity.applicationContext)
            val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(10,10,10,10)
            view.layoutParams = params
            view.text = item.Option
            view.buttonTintList = ContextCompat.
                getColorStateList(this.lessonActivity.applicationContext, R.color.colorGrey)
            TextViewCompat.setTextAppearance(view, R.style.FontForRadioButton)
            view.background = ContextCompat.
                getDrawable(this.lessonActivity.applicationContext, R.drawable.option_background)
            view.setPadding(25,25,25,25)
            view.isClickable = true
            view.tag = index
            rg.addView(view)
        }
    }
}
