package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.helpers.PopupHelper
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.android.flexbox.FlexboxLayout

/**
 * A simple [Fragment] subclass.
 */
class SentenceBuilderFragment(private var lessonActivity: LessonActivity) : Fragment() {

    private lateinit var button: Button
    private var textViewClickListener = View.OnClickListener{}
    private lateinit var multiQuestionTextView: TextView
    private lateinit var sourceFlexBoxLayout: FlexboxLayout
    private lateinit var destFlexBoxLayout: FlexboxLayout
    private var selectedSentence: ArrayList<Int> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sentence_builder, container, false)
        button = view.findViewById(R.id.button_id)
        this.multiQuestionTextView = view.findViewById(R.id.multi_question_id)
        this.sourceFlexBoxLayout = view.findViewById(R.id.flexbox_source_id)
        this.destFlexBoxLayout = view.findViewById(R.id.flexbox_destination_id)
        this.initializeViewClickListener()
        button.setOnClickListener(buttonClickListener)

        this.multiQuestionTextView.text = lessonActivity.dataList[0].Question
        this.setUpView()
        return view
    }

    companion object{
        fun getInstance(lessonActivity: LessonActivity): SentenceBuilderFragment{
            return SentenceBuilderFragment(lessonActivity)
        }
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    //TODO----- experimenting brute force approach -----------
    private fun executeButtonAction() {
        when(lessonActivity.buttonState){
            UserButton.AnswerSelected -> {
                answerQuestion()
            }
            UserButton.NextQuestion -> {
                lessonActivity.navigateToFragment(lessonActivity.dataList[0].LessonFormat)
            }
            else -> {
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

    private fun isCorrectAnswer(): Boolean{
        val sentence = this.buildSentence()
        return sentence == lessonActivity.currentQuestion.CorrectAnswer
    }

    private fun setUpView(){
        lessonActivity.popUpWindow!!.dismiss()
        this.updateOptions()
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
        lessonActivity.setProgressBarStatus()
    }

    //TODO==== end brute force

    private fun initializeViewClickListener(){
        this.textViewClickListener = View.OnClickListener { v ->
            this.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)

            if(this.destFlexBoxLayout.indexOfChild(v) == -1){
                this.sourceFlexBoxLayout.removeView(v)
                this.destFlexBoxLayout.addView(v)
                this.selectedSentence.add(v.tag as Int)
            }else{
                this.destFlexBoxLayout.removeView(v)
                this.sourceFlexBoxLayout.addView(v)
                this.selectedSentence.remove(v.tag as Int)
            }
        }
    }

    private fun updateOptions(){
        lessonActivity.navigateToFragment("MultiSelect")
        this.multiQuestionTextView.text = lessonActivity.dataList[0].Question
        this.sourceFlexBoxLayout.removeAllViews()
        this.destFlexBoxLayout.removeAllViews()
        this.selectedSentence.clear()
        this.buildFlexBoxContent()
    }

    private fun disableOptions(){
        disableViewsFor(this.sourceFlexBoxLayout)
        disableViewsFor(this.destFlexBoxLayout)
    }

    private fun disableViewsFor(flyt: FlexboxLayout){
        for(index in 0 until flyt.childCount){
            val v = flyt.getChildAt(index)
            v.isEnabled = false
        }
    }

    private fun buildSentence(): String{
        val sb = StringBuilder()
        for(item in this.selectedSentence){
            sb.append(lessonActivity.currentQuestion.Options.elementAt(item).Option).append(" ")
        }
        return sb.toString().trim()
    }

    private fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button.text = getString(buttonText)
        lessonActivity.buttonState = buttonState
    }

    private fun buildFlexBoxContent() {
        for((index, item: OptionInfo) in this.lessonActivity.dataList[0].Options.withIndex()){
            val view = TextView(this.lessonActivity.applicationContext)
            val params = FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(10,10,10,10)
            view.layoutParams = params
            view.text = item.Option
            TextViewCompat.setTextAppearance(view, R.style.FontForTextView)
            view.background = ContextCompat.getDrawable(this.lessonActivity.applicationContext,
                R.drawable.word_background)
            view.setPadding(25,25,25,25)
            view.isClickable = true
            view.tag = index
            view.setOnClickListener(this.textViewClickListener)
            this.sourceFlexBoxLayout.addView(view)
        }
    }
}
