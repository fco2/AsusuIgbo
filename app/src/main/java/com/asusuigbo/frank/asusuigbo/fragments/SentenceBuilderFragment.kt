package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.LessonActivity

import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.android.flexbox.FlexboxLayout

/**
 * A simple [Fragment] subclass.
 */
class SentenceBuilderFragment(var lessonActivity: LessonActivity) : Fragment() {

    private lateinit var button: Button
    var textViewClickListener = View.OnClickListener{}
    var multiQuestionTextView: TextView = lessonActivity.activity.findViewById(R.id.multi_question_id)
    private var sourceFlexBoxLayout: FlexboxLayout? = null
    private var destFlexBoxLayout: FlexboxLayout? = null
    private var selectedSentence: ArrayList<Int> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sentence_builder, container, false)
        button = view.findViewById(R.id.button_id)
        button.setOnClickListener(buttonClickListener)
        return view
    }

    companion object{
        fun getInstance(lessonActivity: LessonActivity): SentenceBuilderFragment{
            return SentenceBuilderFragment(lessonActivity)
        }
    }

    private val buttonClickListener = View.OnClickListener {
        //testDictActivity.toggleFragment("B")
    }

    init{
        this.sourceFlexBoxLayout = lessonActivity.activity.findViewById(R.id.flexbox_source_id)
        this.destFlexBoxLayout = lessonActivity.activity.findViewById(R.id.flexbox_destination_id)
        this.initializeViewClickListener()
    }

    private fun initializeViewClickListener(){
        this.textViewClickListener = View.OnClickListener { v ->
            lessonActivity.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)

            if(this.destFlexBoxLayout!!.indexOfChild(v) == -1){
                this.sourceFlexBoxLayout!!.removeView(v)
                this.destFlexBoxLayout!!.addView(v)
                this.selectedSentence.add(v.tag as Int)
            }else{
                this.destFlexBoxLayout!!.removeView(v)
                this.sourceFlexBoxLayout!!.addView(v)
                this.selectedSentence.remove(v.tag as Int)
            }
        }
    }

    fun updateOptions(){
        //lessonActivity.viewDisplayManager("MultiSelect")
        this.multiQuestionTextView.text = lessonActivity.dataList[0].Question
        this.sourceFlexBoxLayout!!.removeAllViews()
        this.destFlexBoxLayout!!.removeAllViews()
        this.selectedSentence.clear()
        DataLoader.buildFlexBoxContent(this)
    }

    fun disableOptions(){
        disableViewsFor(this.sourceFlexBoxLayout!!)
        disableViewsFor(this.destFlexBoxLayout!!)
    }

    private fun disableViewsFor(flyt: FlexboxLayout){
        for(index in 0 until flyt.childCount){
            val v = flyt.getChildAt(index)
            v.isEnabled = false
        }
    }

    fun isCorrectAnswer(): Boolean{
        val sentence = this.buildSentence()
        return sentence == lessonActivity.currentQuestion.CorrectAnswer
    }

    private fun buildSentence(): String{
        val sb = StringBuilder()
        for(item in this.selectedSentence){
            sb.append(lessonActivity.currentQuestion.Options.elementAt(item)).append(" ")
        }
        return sb.toString().trim()
    }

}
