package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.databinding.FragmentWrittenTextBinding
import com.asusuigbo.frank.asusuigbo.helpers.BaseExtendedFragment
import com.asusuigbo.frank.asusuigbo.models.UserButton
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class WrittenTextFragment(var currentLessonActivity: CurrentLessonActivity) : BaseExtendedFragment(currentLessonActivity) {
    private lateinit var binding: FragmentWrittenTextBinding

    constructor() : this(CurrentLessonActivity()){}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_written_text, container, false)
        this.writtenTextChangeListener()
        binding.buttonId.setOnClickListener(buttonClickListener)
        setUpView()
        return binding.root
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    override fun isCorrectAnswer(): Boolean{
        return currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                currentLessonActivity.currentLessonViewModel.selectedAnswer.value!!.toLowerCase(Locale.getDefault()).trim()
    }

    override fun updateOptions(){
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
        currentLessonActivity.currentLessonViewModel.setSelectedAnswer("")
        binding.writtenTextQuestionId.text = currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Question
        binding.writtenTextAnswerId.text.clear()
        binding.writtenTextAnswerId.isEnabled = true
    }

    override fun disableOptions(){
        binding.writtenTextAnswerId.isEnabled = false
    }

    private fun writtenTextChangeListener(){
        binding.writtenTextAnswerId.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                currentLessonActivity.currentLessonViewModel.setSelectedAnswer(p0.toString())
                setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    override fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        binding.buttonId.isEnabled = buttonState != UserButton.AnswerNotSelected
        binding.buttonId.text = getString(buttonText)
        currentLessonActivity.buttonState = buttonState
    }
}
