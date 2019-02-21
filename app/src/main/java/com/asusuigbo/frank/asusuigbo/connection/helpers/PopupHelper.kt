package com.asusuigbo.frank.asusuigbo.connection.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.R

class PopupHelper {
    companion object {
        @SuppressLint("InflateParams", "SetTextI18n")
        fun displaySelectionInPopUp(activity: Activity, layout: RelativeLayout,
                                            correctAnswer: String, isCorrectAnswer: Boolean): PopupWindow{
            val layoutInflater: LayoutInflater =
                    activity.baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val customView = layoutInflater.inflate(R.layout.popup_layout, null)
            val popUpWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, 260)
            popUpWindow.elevation = 10.0f
            val popUpTextResult = customView.findViewById<TextView>(R.id.popup_text_result_id)
            this.stylePopUp(popUpTextResult, customView, activity, correctAnswer, isCorrectAnswer)
            popUpWindow.showAtLocation(layout, Gravity.CENTER_HORIZONTAL, 0, 250)
            return popUpWindow
        }

        private fun stylePopUp(popUpTextResult: TextView, customView: View, activity: Activity,
                               correctAnswer: String, isCorrectAnswer: Boolean) {
            if(isCorrectAnswer){
                popUpTextResult.text = activity.getString(R.string.you_are_correct_text)
            }else{
                popUpTextResult.text = activity.getString(R.string.sorry_wrong_answer_text)
                val rv = customView.findViewById<RelativeLayout>(R.id.custom_view_id)
                val correctAnswerText = customView.findViewById<TextView>(R.id.correct_answer_id)
                rv.setBackgroundColor(ContextCompat.getColor(activity.applicationContext, R.color.wrongAnswer))
                val msg = String.format("%s %s", activity.getString(R.string.answer_template), correctAnswer)
                correctAnswerText!!.text = msg
            }
        }
    }
}