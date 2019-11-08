package com.asusuigbo.frank.asusuigbo.adapters

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.helpers.ItemOffsetDecoration

class ImageChoiceViewAdapter(private val lessonActivity: LessonActivity) {
    var imgChoiceLayout: RelativeLayout = lessonActivity.activity.findViewById(R.id.image_choice_layout_id)
    var imgChoiceQuestion: TextView = lessonActivity.activity.findViewById(R.id.img_choice_question_id)
    var recyclerView: RecyclerView = lessonActivity.activity.findViewById(R.id.img_choice_recycler_view_id)
    val itemOffsetDecoration = ItemOffsetDecoration(lessonActivity.applicationContext, R.dimen.item_offset)
    var isItemDecoratorSet = false

    fun isCorrectAnswer(): Boolean{
        return lessonActivity.selectedAnswer == lessonActivity.currentQuestion.CorrectAnswer
    }

    fun updateOptions(){
        lessonActivity.buildSentenceViewAdapter.multiSelectLayout.visibility = View.GONE
        lessonActivity.writtenTextViewAdapter.writtenTextLayout.visibility = View.GONE
        lessonActivity.singleSelectViewAdapter.singleSelectLayout.visibility = View.GONE
        this.imgChoiceLayout.visibility = View.VISIBLE
        this.imgChoiceQuestion.text = lessonActivity.dataList[0].Question
        lessonActivity.selectedAnswer = ""
        DataLoader.setUpImageChoiceView(lessonActivity)
    }

    fun disableOptions(){
        val rvChildCount = this.recyclerView.childCount
        for(i in 0 until rvChildCount){
            val view: View = this.recyclerView.getChildAt(i)
            view.isClickable = false
        }
    }
}
















