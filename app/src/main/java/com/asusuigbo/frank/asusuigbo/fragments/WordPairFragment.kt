package com.asusuigbo.frank.asusuigbo.fragments


import android.graphics.drawable.TransitionDrawable
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
import com.asusuigbo.frank.asusuigbo.databinding.FragmentWordPairBinding
import com.asusuigbo.frank.asusuigbo.helpers.BaseExtendedFragment
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.android.flexbox.FlexboxLayout

/**
 * A simple [Fragment] subclass.
 */
class WordPairFragment(private var currentLessonActivity: CurrentLessonActivity) : BaseExtendedFragment(currentLessonActivity) {

    //TODO: refactor view to only handle view logic -- it should have its own view model that observes the currentLessonViewModel
    private lateinit var binding: FragmentWordPairBinding

    private var options: MutableList<OptionInfo> = mutableListOf()
    private var totalProcessedWords = 0
    private var chosenWordIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_word_pair, container, false)
        
        this.setUpView()
        binding.buttonId.setOnClickListener(buttonClickListener)
        return binding.root
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    private val textViewClickListener = View.OnClickListener {v ->
        val index: Int = v.tag as Int
        when {
            chosenWordIndex == -1 -> { // means word was chosen first without pair
                setBgdCurrentChosenWord(v as TextView)
                chosenWordIndex = index
            }
            chosenWordIndex == index -> { }
            options[chosenWordIndex].AdditionalInfo == options[index].AdditionalInfo && chosenWordIndex != index -> {
                setBgdPairedWords(v as TextView)
                chosenWordIndex = -1  //reset chosen word index.
            }
            else -> animatePairedWords(v as TextView, R.drawable.animate_wrong_word_pair, true)
        }

        if (totalProcessedWords == options.size)
            answerQuestion()
    }

    private fun setBgdCurrentChosenWord(textView: TextView){
        textView.background = ContextCompat.getDrawable(currentLessonActivity.applicationContext,
            R.drawable.bgd_word_pair_word_selected)
        textView.setTextColor(ContextCompat.getColor(currentLessonActivity.applicationContext,
            R.color.colorWhite))
    }

    private fun setBgdPairedWords(textView: TextView){
        val translatedWord: TextView = binding.flexBoxWordsId.getChildAt(chosenWordIndex) as TextView
        //play audio
        //get data list item with audio, and play it
        val audioUrl = if(options[textView.tag.toString().toInt()].Audio != "")
            options[textView.tag.toString().toInt()].Audio
        else
            options[translatedWord.tag.toString().toInt()].Audio
        if(audioUrl != ""){
            playAudio(audioUrl)
        }
        //animate word pair
        animatePairedWords(textView, R.drawable.animate_correct_word_pair)
        animatePairedWords(translatedWord, R.drawable.animate_correct_word_pair)
    }

    private fun animatePairedWords(translatedWord: TextView, drawable: Int,
                                   isWrongPair: Boolean = false) {
        translatedWord.background = ContextCompat.getDrawable(currentLessonActivity.applicationContext, drawable)
        val anim2 = translatedWord.background as TransitionDrawable
        anim2.startTransition(3000)
        TextViewCompat.setTextAppearance(translatedWord, R.style.FontForTextView)
        if (!isWrongPair)
            disableTextView(translatedWord)
    }

    private fun disableTextView(textView: TextView) {
        textView.isEnabled = false
        totalProcessedWords += 1
    }

    override fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int) {
        binding.buttonId.isEnabled = buttonState != UserButton.AnswerNotSelected
        binding.buttonId.text = getString(buttonText)
        currentLessonActivity.buttonState = buttonState
    }

    override fun isCorrectAnswer(): Boolean = true

    override fun updateOptions() {
        buildFlexBoxContent()
    }

    override fun disableOptions() { return }

    private fun buildFlexBoxContent() {
        this.options = currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.Options
        this.options.shuffle()
        for((index, item: OptionInfo) in this.options.withIndex()){
            val view = TextView(currentLessonActivity.applicationContext)
            val params = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(10,10,10,10)
            view.layoutParams = params
            view.text = item.Option
            TextViewCompat.setTextAppearance(view, R.style.FontForTextView)
            view.background = ContextCompat.getDrawable(currentLessonActivity.applicationContext,
                R.drawable.bgd_word_pair_word)
            view.setPadding(30,30,30,30)
            view.isClickable = true
            view.tag = index
            view.setOnClickListener(this.textViewClickListener)
            binding.flexBoxWordsId.addView(view)
        }
    }
}
