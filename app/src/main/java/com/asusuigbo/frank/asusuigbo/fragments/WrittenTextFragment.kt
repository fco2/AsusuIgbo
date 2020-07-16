package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonViewModel
import com.asusuigbo.frank.asusuigbo.databinding.FragmentWrittenTextBinding
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class WrittenTextFragment : Fragment() {
    private lateinit var binding: FragmentWrittenTextBinding
    private val currentLessonViewModel: CurrentLessonViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_written_text, container, false)
        this.writtenTextChangeListener()
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

    private fun isCorrectAnswer(){
       val isCorrectFlag = currentLessonViewModel.currentQuestion.value!!.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                currentLessonViewModel.selectedAnswer.value!!.toLowerCase(Locale.getDefault()).trim()
       currentLessonViewModel.setIsCorrect(isCorrectFlag)
    }

    private fun updateOptions(){
        currentLessonViewModel.currentQuestion.observe(viewLifecycleOwner, Observer { question ->
            currentLessonViewModel.setSelectedAnswer("")
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
                currentLessonViewModel.setSelectedAnswer(p0.toString())
                currentLessonViewModel.setResetBtnState(true)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}
