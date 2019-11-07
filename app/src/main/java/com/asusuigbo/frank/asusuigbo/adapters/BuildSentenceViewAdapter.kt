package com.asusuigbo.frank.asusuigbo.adapters

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.android.flexbox.FlexboxLayout

class BuildSentenceViewAdapter(private val lessonActivity: LessonActivity) {
    var textViewClickListener = View.OnClickListener{}
    var multiSelectLayout: RelativeLayout = lessonActivity.activity.findViewById(R.id.multi_select_layout_id)
    var multiQuestionTextView: TextView = lessonActivity.activity.findViewById(R.id.multi_question_id)
    private var sourceFlexBoxLayout: FlexboxLayout? = null
    private var destFlexBoxLayout: FlexboxLayout? = null
    private var selectedSentence: ArrayList<Int> = ArrayList()

    init{
        this.sourceFlexBoxLayout = lessonActivity.activity.findViewById(R.id.flexbox_source_id)
        this.destFlexBoxLayout = lessonActivity.activity.findViewById(R.id.flexbox_destination_id)
        this.initializeViewClickListener()
    }

    private fun initializeViewClickListener(){
        this.textViewClickListener = View.OnClickListener { v ->
            if(!lessonActivity.button!!.isEnabled)
                lessonActivity.button!!.isEnabled = true
            lessonActivity.buttonState = UserButton.AnswerSelected

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
        lessonActivity.singleSelectViewAdapter.singleSelectLayout.visibility = View.GONE
        lessonActivity.writtenTextViewAdapter.writtenTextLayout.visibility = View.GONE
        this.multiSelectLayout.visibility = View.VISIBLE
        this.multiQuestionTextView.text = lessonActivity.dataList[0].Question
        this.sourceFlexBoxLayout!!.removeAllViews()
        this.destFlexBoxLayout!!.removeAllViews()
        this.selectedSentence.clear()
        DataLoader.buildFlexBoxContent(lessonActivity)
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

    fun buildSentence(): String{
        val sb = StringBuilder()
        for(item in this.selectedSentence){
            sb.append(lessonActivity.currentQuestion.Options.elementAt(item)).append(" ")
        }
        return sb.toString().trim()
    }
}