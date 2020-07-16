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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonViewModel
import com.asusuigbo.frank.asusuigbo.databinding.FragmentSentenceBuilderBinding
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.google.android.flexbox.FlexboxLayout

/**
 * A simple [Fragment] subclass.
 */
class SentenceBuilderFragment : Fragment(){

    private var textViewClickListener = View.OnClickListener{}
    private var selectedSentence: ArrayList<Int> = ArrayList()
    private var audioUrl = ""

    private lateinit var binding: FragmentSentenceBuilderBinding
    private val currentLessonViewModel: CurrentLessonViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sentence_builder, container, false)
        this.initializeViewClickListener()

        binding.multiQuestionId.text = currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Question
        audioUrl = currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Audio
        updateOptions()
        binding.playAudioId.setOnClickListener(playAudioClickListener)

        if(currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Audio != "")
            binding.playAudioId.visibility = View.VISIBLE
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

    private val playAudioClickListener  = View.OnClickListener {
        currentLessonViewModel.setPlayAudio(audioUrl)
    }

    private fun isCorrectAnswer(){
        val sentence = this.buildSentence()
        currentLessonViewModel.setIsCorrect(sentence == currentLessonViewModel.currentQuestion.value!!.CorrectAnswer)
    }

    private fun initializeViewClickListener(){
        this.textViewClickListener = View.OnClickListener { v ->
            currentLessonViewModel.setResetBtnState(true)

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

    private fun updateOptions(){
        currentLessonViewModel.currentQuestion.observe(viewLifecycleOwner, Observer { question ->
            binding.multiQuestionId.text = question!!.QuestionInfo.Question
            binding.flexboxSourceId.removeAllViews()
            binding.flexboxDestinationId.removeAllViews()
            this.selectedSentence.clear()
            this.buildFlexBoxContent()
        })
    }

    private fun disableOptions(){
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
        this.selectedSentence.forEach{item ->
            sb.append(currentLessonViewModel.currentQuestion.value!!.Options.elementAt(item).Option).append(" ")
        }
        return sb.toString().trim()
    }

    private fun buildFlexBoxContent() {
        for((index, item: OptionInfo) in currentLessonViewModel.currentQuestion.value!!.Options.withIndex()){
            val view = TextView(requireContext().applicationContext)
            val params = FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(10,10,10,10)
            view.layoutParams = params
            view.text = item.Option
            TextViewCompat.setTextAppearance(view, R.style.FontForTextView)
            view.background = ContextCompat.getDrawable(requireContext().applicationContext,
                R.drawable.bgd_white)
            view.setPadding(30,30,30,30)
            view.setTextColor(ContextCompat.getColor(requireContext().applicationContext, R.color.colorPrimaryDark))
            view.isClickable = true
            view.tag = index
            view.setOnClickListener(this.textViewClickListener)
            binding.flexboxSourceId.addView(view)
        }
    }
}
