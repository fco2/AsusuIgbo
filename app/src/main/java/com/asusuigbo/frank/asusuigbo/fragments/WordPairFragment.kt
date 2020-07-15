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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonViewModel
import com.asusuigbo.frank.asusuigbo.databinding.FragmentWordPairBinding
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.google.android.flexbox.FlexboxLayout

/**
 * A simple [Fragment] subclass.
 */
class WordPairFragment : Fragment(){

    private lateinit var binding: FragmentWordPairBinding
    private var options: MutableList<OptionInfo> = mutableListOf()
    private var totalProcessedWords = 0
    private var chosenWordIndex = -1
    private lateinit var currentLesson: CurrentLessonActivity
    private val currentLessonViewModel:CurrentLessonViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentLesson = requireArguments()["currentLesson"] as CurrentLessonActivity
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_word_pair, container, false)
        updateOptions()
        currentLessonViewModel.canAnswerQuestion.observe(viewLifecycleOwner, Observer{ canAnswer ->
            if(canAnswer){
                isCorrectAnswer()
                currentLessonViewModel.setHasCorrectBeenSet(true)
                currentLessonViewModel.setCanAnswerQuestion() //reset it back to false
            }
        })
        return binding.root
    }

    companion object{
        fun getInstance(currentLesson: CurrentLessonActivity) : WordPairFragment{
            val fragment = WordPairFragment()
            val bundle = Bundle()
            bundle.putSerializable("currentLesson", currentLesson)
            fragment.arguments = bundle
            return fragment
        }
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
            currentLessonViewModel.setCanAnswerQuestion()
    }

    private fun setBgdCurrentChosenWord(textView: TextView){
        textView.background = ContextCompat.getDrawable(currentLesson.applicationContext,
            R.drawable.bgd_word_blue)
        textView.setTextColor(ContextCompat.getColor(currentLesson.applicationContext,
            R.color.colorWhite))
    }

    private fun setBgdPairedWords(textView: TextView){
        val translatedWord: TextView = binding.flexBoxWordsId.getChildAt(chosenWordIndex) as TextView
        //get data list item with audio, and play the audio
        val audioUrl = if(options[textView.tag.toString().toInt()].Audio != "")
            options[textView.tag.toString().toInt()].Audio
        else
            options[translatedWord.tag.toString().toInt()].Audio
        if(audioUrl != ""){
            currentLesson.playAudio(audioUrl)
        }
        //animate word pair
        animatePairedWords(textView, R.drawable.animate_correct_word_pair)
        animatePairedWords(translatedWord, R.drawable.animate_correct_word_pair)
    }

    private fun animatePairedWords(translatedWord: TextView, drawable: Int,
                                   isWrongPair: Boolean = false) {
        translatedWord.background = ContextCompat.getDrawable(currentLesson.applicationContext, drawable)
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

    private fun isCorrectAnswer() = currentLessonViewModel.setIsCorrect(true)

    private fun updateOptions() {
        this.options = currentLessonViewModel.currentQuestion.value!!.Options
        this.options.shuffle()
        for((index, item: OptionInfo) in this.options.withIndex()){
            val view = TextView(currentLesson.applicationContext)
            val params = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(10,10,10,10)
            view.layoutParams = params
            view.text = item.Option
            TextViewCompat.setTextAppearance(view, R.style.FontForTextView)
            view.background = ContextCompat.getDrawable(currentLesson.applicationContext,
                R.drawable.bgd_white)
            view.setPadding(30,30,30,30)
            view.isClickable = true
            view.tag = index
            view.setOnClickListener(this.textViewClickListener)
            binding.flexBoxWordsId.addView(view)
        }
    }
}
