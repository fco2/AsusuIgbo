package com.asusuigbo.frank.asusuigbo.adapters.lessons

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.asusuigbo.frank.asusuigbo.models.UserLesson
import kotlinx.android.synthetic.main.lesson_item.view.*


@BindingAdapter("LessonName")
fun TextView.setLessonName(userLesson: UserLesson){
    lessonName.text = userLesson.LessonName
}

@BindingAdapter("LessonImage")
fun ImageView.setLessonImage(userLesson: UserLesson){
    val imageName = userLesson.LessonImage
    val resId: Int = this.context.resources.getIdentifier(imageName,"mipmap", this.context.packageName)
    lessonImage.setImageResource(resId)
}