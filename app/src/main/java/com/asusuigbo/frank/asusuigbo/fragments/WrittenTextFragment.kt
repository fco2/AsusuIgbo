package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.databinding.FragmentWrittenTextBinding
import com.asusuigbo.frank.asusuigbo.models.UserButton
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class WrittenTextFragment : Fragment() {
    private lateinit var binding: FragmentWrittenTextBinding
    private lateinit var currentLesson: CurrentLessonActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentLesson = arguments!!["currentLesson"] as CurrentLessonActivity
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_written_text, container, false)
        this.writtenTextChangeListener()
        updateOptions()
        currentLesson.currentLessonViewModel.canAnswerQuestion.observe(viewLifecycleOwner, Observer{ canAnswer ->
            if(canAnswer){
                disableOptions()
                isCorrectAnswer()
                currentLesson.currentLessonViewModel.setHasCorrectBeenSet(true)
                currentLesson.currentLessonViewModel.setCanAnswerQuestion() //reset it back to false
            }
        })
        return binding.root
    }
    
    companion object{
        fun getInstance(currentLesson: CurrentLessonActivity) : WrittenTextFragment{
            val fragment = WrittenTextFragment()
            val bundle = Bundle()
            bundle.putSerializable("currentLesson", currentLesson)
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun isCorrectAnswer(){
       val isCorrectFlag = currentLesson.currentLessonViewModel.currentQuestion.value!!.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                currentLesson.currentLessonViewModel.selectedAnswer.value!!.toLowerCase(Locale.getDefault()).trim()
        currentLesson.currentLessonViewModel.setIsCorrect(isCorrectFlag)
    }

    private fun updateOptions(){
        currentLesson.currentLessonViewModel.currentQuestion.observe(viewLifecycleOwner, Observer { question ->
            currentLesson.currentLessonViewModel.setSelectedAnswer("")
            binding.writtenTextQuestionId.text = question!!.QuestionInfo.Question
            binding.writtenTextAnswerId.text.clear()
            binding.writtenTextAnswerId.isEnabled = true
        })
    }

    private fun disableOptions(){
        binding.writtenTextAnswerId.isEnabled = false
    }

    private fun writtenTextChangeListener(){
        binding.writtenTextAnswerId.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                currentLesson.currentLessonViewModel.setSelectedAnswer(p0.toString())
                currentLesson.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}
