package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.databinding.FragmentSentenceBuilderBinding
import com.asusuigbo.frank.asusuigbo.helpers.BaseExtendedFragment
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.android.flexbox.FlexboxLayout

/**
 * A simple [Fragment] subclass.
 */
class SentenceBuilderFragment(private var currentLessonActivity: CurrentLessonActivity) : BaseExtendedFragment(currentLessonActivity) {

    private var textViewClickListener = View.OnClickListener{}
    private var selectedSentence: ArrayList<Int> = ArrayList()
    private var audioUrl = ""

    private lateinit var binding: FragmentSentenceBuilderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sentence_builder, container, false)
        this.initializeViewClickListener()
        binding.buttonId.setOnClickListener(buttonClickListener)

        binding.multiQuestionId.text = currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Question
        audioUrl = currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Audio
        this.setUpView()
        binding.playAudioId.setOnClickListener(playAudioClickListener)

        if(currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Audio != "")
            binding.playAudioId.visibility = View.VISIBLE
        return view
    }

    private val playAudioClickListener  = View.OnClickListener {
        playAudio(audioUrl)
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    override fun isCorrectAnswer(): Boolean{
        val sentence = this.buildSentence()
        return sentence == currentLessonActivity.currentQuestion.CorrectAnswer
    }

    private fun initializeViewClickListener(){
        this.textViewClickListener = View.OnClickListener { v ->
            this.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)

            if(binding.flexboxDestinationId.indexOfChild(v) == -1){
                binding.flexboxSourceId.removeView(v)
                binding.flexboxDestinationId.addView(v)
                this.selectedSentence.add(v.tag as Int)
            }else{
                binding.flexboxDestinationId.removeView(v)
                binding.flexboxSourceId.addView(v)
                this.selectedSentence.remove(v.tag as Int)
            }
        }
    }

    override fun updateOptions(){
        binding.multiQuestionId.text = currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Question
        binding.flexboxSourceId.removeAllViews()
        binding.flexboxDestinationId.removeAllViews()
        this.selectedSentence.clear()
        this.buildFlexBoxContent()
    }

    override fun disableOptions(){
        disableViewsFor(binding.flexboxSourceId)
        disableViewsFor(binding.flexboxDestinationId)
    }

    private fun disableViewsFor(layout: FlexboxLayout){
        for(index in 0 until layout.childCount){
            val v = layout.getChildAt(index)
            v.isEnabled = false
        }
    }

    private fun buildSentence(): String{
        val sb = StringBuilder()
        for(item in this.selectedSentence){
            sb.append(currentLessonActivity.currentQuestion.Options.elementAt(item).Option).append(" ")
        }
        return sb.toString().trim()
    }

    override fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        binding.buttonId.isEnabled = buttonState != UserButton.AnswerNotSelected
        binding.buttonId.text = getString(buttonText)
        currentLessonActivity.buttonState = buttonState
    }

    private fun buildFlexBoxContent() {
        for((index, item: OptionInfo) in this.currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.Options.withIndex()){
            val view = TextView(this.currentLessonActivity.applicationContext)
            val params = FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(10,10,10,10)
            view.layoutParams = params
            view.text = item.Option
            TextViewCompat.setTextAppearance(view, R.style.FontForTextView)
            view.background = ContextCompat.getDrawable(this.currentLessonActivity.applicationContext,
                R.drawable.bgd_word_pair_word)
            view.setPadding(30,30,30,30)
            view.setTextColor(ContextCompat.getColor(currentLessonActivity, R.color.colorPrimaryDark))
            view.isClickable = true
            view.tag = index
            view.setOnClickListener(this.textViewClickListener)
            binding.flexboxSourceId.addView(view)
        }
    }
}
