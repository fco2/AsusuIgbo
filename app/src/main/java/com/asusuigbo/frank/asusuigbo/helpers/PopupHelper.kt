package com.asusuigbo.frank.asusuigbo.helpers

import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import kotlinx.android.synthetic.main.activity_lesson.*

class PopupHelper {
    companion object {
        fun displaySelectionInPopUp(lessonActivity: LessonActivity, isCorrectAnswer: Boolean): PopupWindow{
            val layoutInflater: LayoutInflater =
                    lessonActivity.activity.baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val customView = layoutInflater.inflate(R.layout.component_popup_layout, lessonActivity.activity.lessons_layout_id,
                    false)

            val display = lessonActivity.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            val popUpWindow = PopupWindow(customView, (width * 0.8).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            popUpWindow.elevation = 10.0f
            val popUpTextResult = customView.findViewById<TextView>(R.id.popup_text_result_id)
            stylePopUp(
                popUpTextResult,
                customView,
                lessonActivity,
                isCorrectAnswer
            )
            popUpWindow.showAtLocation(lessonActivity.lessonsLayout, Gravity.BOTTOM, 0, 500)
            return popUpWindow
        }

        private fun stylePopUp(popUpTextResult: TextView, customView: View, lessonActivity: LessonActivity,
                               isCorrectAnswer: Boolean) {
            val popUpLayout = customView.findViewById<ConstraintLayout>(R.id.custom_view_id)
            val correctAnswerText = customView.findViewById<TextView>(R.id.correct_answer_id)
            val image = customView.findViewById<ImageView>(R.id.pop_up_image_id)
            if(isCorrectAnswer){
                popUpTextResult.text = lessonActivity.activity.getString(R.string.you_are_correct_text)
                correctAnswerText.visibility = View.GONE
            }else{
                popUpTextResult.text = lessonActivity.activity.getString(R.string.correct_answer_text)
                image.setImageResource(R.mipmap.img_wrong_answer)
                val wrongAnswerColor = ContextCompat.getColor(lessonActivity.activity.applicationContext, R.color.wrongAnswer)
                popUpLayout.setBackgroundColor(wrongAnswerColor)
                correctAnswerText!!.text = lessonActivity.currentQuestion.CorrectAnswer
            }
        }
    }
}