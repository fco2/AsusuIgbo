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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonViewModel
import com.asusuigbo.frank.asusuigbo.databinding.FragmentSingleSelectBinding
import com.asusuigbo.frank.asusuigbo.models.UserButton
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SingleSelectFragment : Fragment() {

    private lateinit var binding: FragmentSingleSelectBinding
    private lateinit var currentLesson: CurrentLessonActivity
    private val currentLessonViewModel: CurrentLessonViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentLesson = requireArguments()["currentLesson"] as CurrentLessonActivity
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_select, container, false)
        binding.questionText.text = currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Question
        updateOptions()
        binding.playAudioBtn.setOnClickListener(playAudioClickListener)
        //set visibility of play button
        if(currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Audio != "")
            binding.playAudioBtn.visibility = View.VISIBLE

        updateOptions()
        currentLessonViewModel.canAnswerQuestion.observe(viewLifecycleOwner, Observer{ canAnswer ->
            if(canAnswer){
                disableOptions()
                isCorrectAnswer()
                currentLessonViewModel.setHasCorrectBeenSet(true)
                currentLessonViewModel.setCanAnswerQuestion() //reset it back to false
            }
        })

        return binding.root
    }

    companion object{
        fun getInstance(currentLesson: CurrentLessonActivity): SingleSelectFragment{
            val fragment = SingleSelectFragment()
            val bundle = Bundle()
            bundle.putSerializable("currentLesson", currentLesson)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val playAudioClickListener  = View.OnClickListener {
        currentLesson.playAudio(currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Audio)
    }

    private fun isCorrectAnswer(){
        val result = currentLessonViewModel.currentQuestion.value!!.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                currentLessonViewModel.selectedAnswer.value!!.toLowerCase(Locale.getDefault()).trim()
        currentLessonViewModel.setIsCorrect(result)
    }

    private fun updateOptions(){
        currentLessonViewModel.currentQuestion.observe(viewLifecycleOwner, Observer { question ->
            binding.questionText.text = question!!.QuestionInfo.Question
            binding.choicesRadioGroup.removeAllViews()
            currentLessonViewModel.setSelectedAnswer("")
            this.buildRadioGroupContent()
        })
    }

    private fun disableOptions(){
        for(index in 0 until binding.choicesRadioGroup.childCount){
            val v = binding.choicesRadioGroup.getChildAt(index)
            v.isEnabled = false
        }
    }

    private fun buildRadioGroupContent(){
        for((index,item) in currentLessonViewModel.currentQuestion.value!!.Options.withIndex()){
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
            view.setOnClickListener {
                currentLessonViewModel.setSelectedAnswer(view.text.toString())
                currentLesson.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
            }
            view.tag = index
            binding.choicesRadioGroup.addView(view)
        }
    }
}
