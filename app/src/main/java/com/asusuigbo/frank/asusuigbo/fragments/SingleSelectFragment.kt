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
import com.asusuigbo.frank.asusuigbo.helpers.PopupHelper
import com.asusuigbo.frank.asusuigbo.models.UserButton
import java.util.*

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

    override fun onStart() {
        super.onStart()
        setUpView()
    }

    companion object{
        fun getInstance(lessonActivity: LessonActivity): SingleSelectFragment{
            return SingleSelectFragment(lessonActivity)
        }
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    //TODO------ experimenting brute force design --------------
    private fun executeButtonAction() {
        when(lessonActivity.buttonState){
            UserButton.AnswerSelected -> {
                answerQuestion()
            }
            UserButton.NextQuestion -> {
                //TODO: here, call lessonActivity to switch fragments
                //nextQuestion()
                lessonActivity.navigateToFragment(lessonActivity.dataList[0].LessonFormat)
            }
            else -> {
                //TODO: here call lesson activity to switch fragments to finish quiz
                //finishQuiz()
                lessonActivity.navigateToFragment()
            }
        }
    }

    private fun answerQuestion(){
        lessonActivity.currentQuestion = lessonActivity.dataList.removeAt(0)
        disableOptions()
        lessonActivity.popUpWindow =
            PopupHelper.displaySelectionInPopUp(lessonActivity, this.isCorrectAnswer())

        if(!this.isCorrectAnswer())
            lessonActivity.dataList.add(lessonActivity.currentQuestion)
        if(lessonActivity.dataList.size > 0)
            this.setUpButtonStateAndText(UserButton.NextQuestion, R.string.next_question_text)
        else
            this.setUpButtonStateAndText(UserButton.Finished, R.string.continue_text)
    }

    private fun isCorrectAnswer(): Boolean {
        return lessonActivity.currentQuestion.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                lessonActivity.selectedAnswer.toLowerCase(Locale.getDefault()).trim()
    }

    private fun setUpView(){
        if(lessonActivity.popUpWindow != null)
            lessonActivity.popUpWindow!!.dismiss()
        this.updateOptions()
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
        lessonActivity.setProgressBarStatus()
    }

    private fun updateOptions(){
        //lessonActivity.navigateToFragment("SingleSelect")
        this.singleQuestionTextView!!.text = lessonActivity.dataList[0].Question
        this.radioGroup.removeAllViews()
        lessonActivity.selectedAnswer = ""
        this.buildRadioGroupContent()
    }

    private fun disableOptions(){
        for(index in 0 until this.radioGroup.childCount){
            val v = this.radioGroup.getChildAt(index)
            v.isEnabled = false
        }
    }

    //TODo------- end brute force -------

    private fun invokeCheckedButtonListener(){
        radioGroup.setOnCheckedChangeListener{ group, checkedId ->
            if(group.checkedRadioButtonId != -1){ //if it is a valid checked radio button
                val checkedRadioBtn: RadioButton = view!!.findViewById(checkedId)
                lessonActivity.selectedAnswer = checkedRadioBtn.text.toString()
                this.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
            }
        }
    }

    fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button.text = getString(buttonText)
        lessonActivity.buttonState = buttonState
    }

    private fun buildRadioGroupContent(){
        //Attempt to invoke virtual method 'void android.widget.RadioGroup.clearCheck()' on a null object reference
        this.radioGroup.clearCheck() // <--
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
            this.radioGroup.addView(view)
        }
    }
}
