package com.asusuigbo.frank.asusuigbo.adapters.lessons

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.asusuigbo.frank.asusuigbo.models.LessonInfo
import kotlinx.android.synthetic.main.lesson_item.view.*


@BindingAdapter("lessonKey")
fun TextView.setLessonKey(lessonInfo: LessonInfo){
    lessonName.text = lessonInfo.lessonKey
}

@BindingAdapter("lessonImage")
fun ImageView.setLessonImage(lessonInfo: LessonInfo){
    val imageName = lessonInfo.imageDrawableIndex
    val resId: Int = context.resources.getIdentifier(imageName,"mipmap", context.packageName)
    lessonImage.setImageResource(resId)
}