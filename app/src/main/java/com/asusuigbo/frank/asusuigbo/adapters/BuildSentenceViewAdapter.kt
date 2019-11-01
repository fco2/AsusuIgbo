package com.asusuigbo.frank.asusuigbo.adapters

import android.view.View
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.android.flexbox.FlexboxLayout

class BuildSentenceViewAdapter(private val lessonActivity: LessonActivity) {

    fun initializeViewClickListener(){
        lessonActivity.textViewClickListener = View.OnClickListener { v ->
            if(!lessonActivity.button!!.isEnabled)
                lessonActivity.button!!.isEnabled = true
            lessonActivity.buttonState = UserButton.AnswerSelected

            if(lessonActivity.destFlexBoxLayout!!.indexOfChild(v) == -1){
                lessonActivity.sourceFlexBoxLayout!!.removeView(v)
                lessonActivity.destFlexBoxLayout!!.addView(v)
                lessonActivity.selectedSentence.add(v.tag as Int)
            }else{
                lessonActivity.destFlexBoxLayout!!.removeView(v)
                lessonActivity.sourceFlexBoxLayout!!.addView(v)
                lessonActivity.selectedSentence.remove(v.tag as Int)
            }
        }
    }

    fun updateOptions(){
        lessonActivity.singleSelectLayout.visibility = View.GONE
        lessonActivity.writtenTextLayout.visibility = View.GONE
        lessonActivity.multiSelectLayout.visibility = View.VISIBLE
        lessonActivity.multiQuestionTextView.text = lessonActivity.dataList[0].Question
        lessonActivity.sourceFlexBoxLayout!!.removeAllViews()
        lessonActivity.destFlexBoxLayout!!.removeAllViews()
        lessonActivity.selectedSentence.clear()
        DataLoader.buildFlexBoxContent(lessonActivity)
    }

    fun disableOptions(){
        disableViewsFor(lessonActivity.sourceFlexBoxLayout!!)
        disableViewsFor(lessonActivity.destFlexBoxLayout!!)
    }

    private fun disableViewsFor(flyt: FlexboxLayout){
        for(index in 0 until flyt.childCount){
            val v = flyt.getChildAt(index)
            v.isEnabled = false
        }
    }

    fun buildSentence(): String{
        val sb = StringBuilder()
        for(item in lessonActivity.selectedSentence){
            sb.append(lessonActivity.currentQuestion.Options.elementAt(item)).append(" ")
        }
        return sb.toString().trim()
    }
}