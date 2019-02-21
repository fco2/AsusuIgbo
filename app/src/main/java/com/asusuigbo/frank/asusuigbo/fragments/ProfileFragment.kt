package com.asusuigbo.frank.asusuigbo.fragments

import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.connection.helpers.PopupHelper
import com.asusuigbo.frank.asusuigbo.connection.helpers.SentenceCreatorHelper
import com.asusuigbo.frank.asusuigbo.models.SentenceInfo
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    var sourceFlexBoxLayout: FlexboxLayout? = null
    var destFlexBoxLayout: FlexboxLayout? = null
    //TODO: Refactor names to proper naming convention
    private var sentenceList: ArrayList<SentenceInfo> = ArrayList()
    private var workingList: ArrayList<SentenceInfo> = ArrayList()
    private lateinit var currentQuestion: SentenceInfo
    private var selectedSentence: ArrayList<Int> = ArrayList()
    private var button: Button? = null
    private var textView: TextView? = null
    private var buttonState: UserButton = UserButton.AnswerNotSelected
    private lateinit var profileLayout: RelativeLayout
    private var popUpWindow: PopupWindow? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_profile, container, false)
        sourceFlexBoxLayout = view.findViewById(R.id.flexbox_source_id)
        destFlexBoxLayout = view.findViewById(R.id.flexbox_destination_id)
        button = view.findViewById(R.id.check_answer_button_id)
        textView = view.findViewById(R.id.profile_question_id)
        profileLayout = view.findViewById(R.id.profile_layout_id)

        SentenceCreatorHelper.populateList(sentenceList, workingList, view, activity, textViewClickListener)
        button!!.setOnClickListener(buttonClickListener)
        return view
    }

    private val buttonClickListener = View.OnClickListener {
        when(this.buttonState){
            UserButton.AnswerSelected -> {
                answerQuestion()
            }
            UserButton.NextQuestion -> {
                nextQuestion()
            }
            else -> {
                finishQuiz()
            }
        }
    }

    private val textViewClickListener = View.OnClickListener { v ->
        if(!button!!.isEnabled)
            button!!.isEnabled = true
        this.buttonState = UserButton.AnswerSelected

        if(destFlexBoxLayout!!.indexOfChild(v) == -1){
            sourceFlexBoxLayout!!.removeView(v)
            destFlexBoxLayout!!.addView(v)
            selectedSentence.add(v.tag as Int)
        }else{
            destFlexBoxLayout!!.removeView(v)
            sourceFlexBoxLayout!!.addView(v)
            selectedSentence.remove(v.tag as Int)
        }
    }

    private fun answerQuestion(){
        disableOptions()
        //if(fullListSize == 0)
        //    fullListSize = workingList.size
        this.currentQuestion = this.workingList.removeAt(0)
        this.popUpWindow = PopupHelper.displaySelectionInPopUp(activity, profileLayout,
                currentQuestion.correctAnswer, isCorrectAnswer())

        if(!this.isCorrectAnswer())
            this.workingList.add(this.currentQuestion)

        if(this.workingList.size > 0)
            this.setUpButtonStateAndText(UserButton.NextQuestion, R.string.next_question_text)
        else
            this.setUpButtonStateAndText(UserButton.Finished, R.string.continue_text)
    }

    private fun nextQuestion(){
        popUpWindow!!.dismiss()
        this.updateOptions()
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
        //setProgressBarStatus()
    }

    private fun updateOptions(){
        textView!!.text = workingList[0].fullSentence
        this.sourceFlexBoxLayout!!.removeAllViews()
        this.destFlexBoxLayout!!.removeAllViews()
        this.selectedSentence.clear()
        SentenceCreatorHelper.buildFlexBoxContent(workingList[0].wordBlocks, sourceFlexBoxLayout!!,
                activity, textViewClickListener)
    }

    private fun finishQuiz(){
        popUpWindow!!.dismiss()
        //updateCompletedLesson()
        launchCompletedLessonScreen()
    }

    private fun updateCompletedLesson(){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val dbReference: DatabaseReference = database.getReference("TableOfContent/Items")
        //dbReference.child(this.nextLesson).child("LessonComplete").setValue("TRUE")
    }

    private fun disableOptions(){
        disableViewsFor(sourceFlexBoxLayout!!)
        disableViewsFor(destFlexBoxLayout!!)
    }

    private fun disableViewsFor(flyt: FlexboxLayout){
        for(index in 0 until flyt.childCount){
            val v = flyt.getChildAt(index)
            v.isEnabled = false
        }
    }

    private fun launchCompletedLessonScreen(){
        val fm: FragmentManager = fragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.add(android.R.id.content, LessonCompletedFragment())
        ft.commit()
    }

    private fun buildSentence(): String{
        val sb = StringBuilder()
        for(item in selectedSentence){
            sb.append(currentQuestion.wordBlocks.elementAt(item)).append(" ")
        }
        return sb.toString().trim()
    }

    private fun isCorrectAnswer(): Boolean{
        val sentence = this.buildSentence()
        return sentence == currentQuestion.correctAnswer
    }

    private fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button!!.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button!!.text = getString(buttonText)
        this.buttonState = buttonState
    }
}// Required empty public constructor
