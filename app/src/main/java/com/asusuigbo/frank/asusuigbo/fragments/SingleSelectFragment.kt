package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.widget.TextViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonViewModel
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonViewModelFactory
import com.asusuigbo.frank.asusuigbo.databinding.FragmentSingleSelectBinding
import com.asusuigbo.frank.asusuigbo.helpers.BaseExtendedFragment
import com.asusuigbo.frank.asusuigbo.models.UserButton
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SingleSelectFragment(private var currentLesson: CurrentLessonActivity) : BaseExtendedFragment(currentLesson) {

    private lateinit var binding: FragmentSingleSelectBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_select, container, false)
        this.invokeCheckedButtonListener()
        binding.buttonId.setOnClickListener(buttonClickListener)
        binding.questionText.text = currentLesson.currentLessonViewModel.questionList.value!![0].QuestionInfo.Question
        this.setUpView()
        binding.playAudioBtn.setOnClickListener(playAudioClickListener)
        //set visibility of play button
        if(currentLesson.currentLessonViewModel.questionList.value!![0].QuestionInfo.Audio != "")
            binding.playAudioBtn.visibility = View.VISIBLE
        return binding.root
    }

    private val playAudioClickListener  = View.OnClickListener {
        playAudio(currentLesson.currentLessonViewModel.questionList.value!![0].QuestionInfo.Audio)
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    override fun isCorrectAnswer(): Boolean {
        return currentLesson.currentQuestion.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                currentLesson.selectedAnswer.toLowerCase(Locale.getDefault()).trim()
    }

    override fun updateOptions(){
        binding.questionText.text = currentLesson.currentLessonViewModel.questionList.value!![0].QuestionInfo.Question
        binding.choicesRadioGroup.removeAllViews()
        currentLesson.selectedAnswer = ""
        this.buildRadioGroupContent()
    }

    override fun disableOptions(){
        for(index in 0 until binding.choicesRadioGroup.childCount){
            val v = binding.choicesRadioGroup.getChildAt(index)
            v.isEnabled = false
        }
    }

    private fun invokeCheckedButtonListener(){
        binding.choicesRadioGroup.setOnCheckedChangeListener{ group, checkedId ->
            if(group.checkedRadioButtonId != -1){ //if it is a valid checked radio button
                //TODO: test if this works
                val checkedRadioBtn: RadioButton = binding.choicesRadioGroup[checkedId] as RadioButton //view!!.findViewById(checkedId)
                currentLesson.selectedAnswer = checkedRadioBtn.text.toString()
                this.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
            }
        }
    }

    override fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        binding.buttonId.isEnabled = buttonState != UserButton.AnswerNotSelected
        binding.buttonId.text = getString(buttonText)
        currentLesson.buttonState = buttonState
    }

    private fun buildRadioGroupContent(){
        for((index,item) in currentLesson.currentLessonViewModel.questionList.value!![0].Options.withIndex()){
            val view = RadioButton(this.currentLesson.applicationContext)
            val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(10,10,10,10)
            view.layoutParams = params
            view.text = item.Option
            view.buttonTintList = ContextCompat.
                getColorStateList(this.currentLesson.applicationContext, R.color.colorGrey)
            TextViewCompat.setTextAppearance(view, R.style.FontForRadioButton)
            view.background = ContextCompat.
                getDrawable(this.currentLesson.applicationContext, R.drawable.option_background)
            view.setPadding(25,25,25,25)
            view.isClickable = true
            view.tag = index
            binding.choicesRadioGroup.addView(view)
        }
    }
}
