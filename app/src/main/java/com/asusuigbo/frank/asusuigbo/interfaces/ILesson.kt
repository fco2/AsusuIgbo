package com.asusuigbo.frank.asusuigbo.interfaces

import android.app.Activity
import android.view.View
import android.widget.ProgressBar
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup

interface ILesson {
    var dataList: ArrayList<QuestionGroup>
    var requestedLesson: String
    var activity: Activity
    var progressBar: ProgressBar?
    var textViewClickListener: View.OnClickListener

    fun isCorrectAnswer() : Boolean
}