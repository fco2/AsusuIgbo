package com.asusuigbo.frank.asusuigbo.fragments


import android.graphics.drawable.TransitionDrawable
import android.media.MediaPlayer
import android.os.Bundle
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
import com.asusuigbo.frank.asusuigbo.helpers.BaseExtendedFragment
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.storage.FirebaseStorage

/**
 * A simple [Fragment] subclass.
 */
class WordPairFragment(private var lessonActivity: LessonActivity) : BaseExtendedFragment(lessonActivity) {

    private var dataList: MutableList<OptionInfo> = mutableListOf()
    private var totalProcessedWords = 0
    private lateinit var flexBoxLayout: FlexboxLayout
    private var chosenWordIndex = -1
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_word_pair, container, false)
        button = view.findViewById(R.id.button_id)
        this.flexBoxLayout = view.findViewById(R.id.flex_box_words_id)
        this.setUpView()
        button.setOnClickListener(buttonClickListener)
        return view
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
            dataList[chosenWordIndex].AdditionalInfo == dataList[index].AdditionalInfo -> {
                setBgdPairedWords(v as TextView)
                chosenWordIndex = -1  //reset chosen word index.
            }
            else -> animatePairedWords(v as TextView, R.drawable.animate_wrong_word_pair, true)
        }

        if (totalProcessedWords == dataList.size)
            answerQuestion()
    }

    private fun setBgdCurrentChosenWord(textView: TextView){
        textView.background = ContextCompat.getDrawable(lessonActivity.applicationContext,
            R.drawable.bgd_word_pair_word_selected)
        textView.setTextColor(ContextCompat.getColor(lessonActivity.applicationContext,
            R.color.colorWhite))
    }

    private fun setBgdPairedWords(textView: TextView){
        val translatedWord: TextView = this.flexBoxLayout.getChildAt(chosenWordIndex) as TextView
        animatePairedWords(textView, R.drawable.animate_correct_word_pair)
        animatePairedWords(translatedWord, R.drawable.animate_correct_word_pair)
        //play audio
        //get data list item with audio, and play it
        val audioUrl = if(dataList[textView.tag.toString().toInt()].Audio != "")
            dataList[textView.tag.toString().toInt()].Audio
        else
            dataList[translatedWord.tag.toString().toInt()].Audio

        //TODO: this logic should be extracted to abstract class.
        val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child(audioUrl).downloadUrl.addOnSuccessListener {
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(it.toString())
            mediaPlayer.setOnPreparedListener{player ->
                player.start()
            }
            mediaPlayer.prepareAsync()
        }
    }


    private fun animatePairedWords(translatedWord: TextView, drawable: Int,
                                   isWrongPair: Boolean = false) {
        translatedWord.background = ContextCompat.getDrawable(lessonActivity.applicationContext, drawable)
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
        this.button.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button.text = getString(buttonText)
        lessonActivity.buttonState = buttonState
    }

    override fun isCorrectAnswer(): Boolean = true

    override fun updateOptions() {
        buildFlexBoxContent()
    }

    override fun disableOptions() { return }

    private fun buildFlexBoxContent() {
        this.dataList = this.lessonActivity.dataList[0].Options
        this.dataList.shuffle()
        for((index, item: OptionInfo) in this.dataList.withIndex()){
            val view = TextView(lessonActivity.applicationContext)
            val params = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(10,10,10,10)
            view.layoutParams = params
            view.text = item.Option
            TextViewCompat.setTextAppearance(view, R.style.FontForTextView)
            view.background = ContextCompat.getDrawable(lessonActivity.applicationContext,
                R.drawable.bgd_word_pair_word)
            view.setPadding(30,30,30,30)
            view.isClickable = true
            view.tag = index
            view.setOnClickListener(this.textViewClickListener)
            this.flexBoxLayout.addView(view)
        }
    }
}
