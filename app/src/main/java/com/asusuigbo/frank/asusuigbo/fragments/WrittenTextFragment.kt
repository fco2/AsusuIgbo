package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.LessonActivity

import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.UserButton

/**
 * A simple [Fragment] subclass.
 */
class WrittenTextFragment(var lessonActivity: LessonActivity) : Fragment() {

    private lateinit var button: Button
    var writtenTextQuestion: TextView? = null
    private lateinit var writtenTextAnswer: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_written_text, container, false)
        button = view.findViewById(R.id.button_id)
        this.writtenTextQuestion = view.findViewById(R.id.written_text_question_id)
        this.writtenTextAnswer = view.findViewById(R.id.written_text_answer_id)
        this.writtenTextChangeListener()
        button.setOnClickListener(buttonClickListener)

        this.writtenTextQuestion!!.text = lessonActivity.dataList[0].Question

        return view
    }

    companion object{
        fun getInstance(lessonActivity: LessonActivity): WrittenTextFragment{
            return WrittenTextFragment(lessonActivity)
        }
    }

    private val buttonClickListener = View.OnClickListener {
        lessonActivity.executeButtonAction()
    }

    fun updateOptions(){
        lessonActivity.navigateToFragment("WrittenText")
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
        lessonActivity.selectedAnswer = ""
        this.writtenTextQuestion!!.text = lessonActivity.dataList[0].Question
        this.writtenTextAnswer.text.clear()
        this.writtenTextAnswer.isEnabled = true
    }

    fun disableOptions(){
        this.writtenTextAnswer.isEnabled = false
    }

    private fun writtenTextChangeListener(){
        this.writtenTextAnswer.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                lessonActivity.selectedAnswer = p0.toString()
                setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button.text = getString(buttonText)
        lessonActivity.buttonState = buttonState
    }

}
