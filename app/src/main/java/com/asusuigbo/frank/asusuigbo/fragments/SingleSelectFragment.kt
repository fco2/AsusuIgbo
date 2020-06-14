package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
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
        binding.buttonId.setOnClickListener(buttonClickListener)
        binding.questionText.text = currentLesson.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Question
        this.setUpView()
        binding.playAudioBtn.setOnClickListener(playAudioClickListener)
        //set visibility of play button
        if(currentLesson.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Audio != "")
            binding.playAudioBtn.visibility = View.VISIBLE
        return binding.root
    }

    private val playAudioClickListener  = View.OnClickListener {
        playAudio(currentLesson.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Audio)
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    override fun isCorrectAnswer(): Boolean {
        return currentLesson.currentLessonViewModel.currentQuestion.value!!.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                currentLesson.currentLessonViewModel.selectedAnswer.value!!.toLowerCase(Locale.getDefault()).trim()
    }

    override fun updateOptions(){
        binding.questionText.text = currentLesson.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Question
        binding.choicesRadioGroup.removeAllViews()
        currentLesson.currentLessonViewModel.setSelectedAnswer("")
        this.buildRadioGroupContent()
    }

    override fun disableOptions(){
        for(index in 0 until binding.choicesRadioGroup.childCount){
            val v = binding.choicesRadioGroup.getChildAt(index)
            v.isEnabled = false
        }
    }

    override fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        binding.buttonId.isEnabled = buttonState != UserButton.AnswerNotSelected
        binding.buttonId.text = getString(buttonText)
        currentLesson.buttonState = buttonState
    }

    private fun buildRadioGroupContent(){
        for((index,item) in currentLesson.currentLessonViewModel.currentQuestion.value!!.Options.withIndex()){
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
            view.setOnClickListener { setSelectedAnswer(view.text.toString())}
            view.tag = index
            binding.choicesRadioGroup.addView(view)
        }
    }

    private fun setSelectedAnswer(s: String){
        currentLesson.currentLessonViewModel.setSelectedAnswer(s)
        this.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
    }
}
