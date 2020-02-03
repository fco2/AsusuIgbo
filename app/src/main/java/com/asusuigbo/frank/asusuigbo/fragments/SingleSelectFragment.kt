package com.asusuigbo.frank.asusuigbo.fragments


import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.helpers.BaseExtendedFragment
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.firebase.storage.FirebaseStorage
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SingleSelectFragment(private var lessonActivity: LessonActivity) : BaseExtendedFragment(lessonActivity) {

    private lateinit var button: Button
    private lateinit var radioGroup: RadioGroup
    private var singleQuestionTextView: TextView? = null
    private lateinit var playAudioBtn: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_single_select, container, false)
        button = view.findViewById(R.id.button_id)
        radioGroup = view.findViewById(R.id.radio_group_id)
        this.invokeCheckedButtonListener()
        singleQuestionTextView = view.findViewById(R.id.single_question_id)
        playAudioBtn = view.findViewById(R.id.play_audio_id)
        button.setOnClickListener(buttonClickListener)

        lessonActivity.singleSelectFragment.singleQuestionTextView!!.text =
            lessonActivity.dataList[0].QuestionInfo.Question
        this.setUpView()
        playAudioBtn.setOnClickListener(playAudioClickListener)

        //set visibility of play button
        if(lessonActivity.dataList[0].QuestionInfo.Audio != "")
            playAudioBtn.visibility = View.VISIBLE
        return view
    }

    //TODO: can be embedded within abstract class
    private val playAudioClickListener  = View.OnClickListener {
        val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child(lessonActivity.dataList[0].QuestionInfo.Audio).
            downloadUrl.addOnSuccessListener {
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(it.toString())
            mediaPlayer.setOnPreparedListener{player ->
                player.start()
            }
            mediaPlayer.prepareAsync()
        }
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    override fun isCorrectAnswer(): Boolean {
        return lessonActivity.currentQuestion.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                lessonActivity.selectedAnswer.toLowerCase(Locale.getDefault()).trim()
    }

    override fun updateOptions(){
        this.singleQuestionTextView!!.text = lessonActivity.dataList[0].QuestionInfo.Question
        this.radioGroup.removeAllViews()
        lessonActivity.selectedAnswer = ""
        this.buildRadioGroupContent()
    }

    override fun disableOptions(){
        for(index in 0 until this.radioGroup.childCount){
            val v = this.radioGroup.getChildAt(index)
            v.isEnabled = false
        }
    }

    private fun invokeCheckedButtonListener(){
        radioGroup.setOnCheckedChangeListener{ group, checkedId ->
            if(group.checkedRadioButtonId != -1){ //if it is a valid checked radio button
                val checkedRadioBtn: RadioButton = view!!.findViewById(checkedId)
                lessonActivity.selectedAnswer = checkedRadioBtn.text.toString()
                this.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
            }
        }
    }

    override fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button.text = getString(buttonText)
        lessonActivity.buttonState = buttonState
    }

    private fun buildRadioGroupContent(){
        for((index,item) in this.lessonActivity.dataList[0].Options.withIndex()){
            val view = RadioButton(this.lessonActivity.applicationContext)
            val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(10,10,10,10)
            view.layoutParams = params
            view.text = item.Option
            view.buttonTintList = ContextCompat.
                getColorStateList(this.lessonActivity.applicationContext, R.color.colorGrey)
            TextViewCompat.setTextAppearance(view, R.style.FontForRadioButton)
            view.background = ContextCompat.
                getDrawable(this.lessonActivity.applicationContext, R.drawable.option_background)
            view.setPadding(25,25,25,25)
            view.isClickable = true
            view.tag = index
            this.radioGroup.addView(view)
        }
    }
}
