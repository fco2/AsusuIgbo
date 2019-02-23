package com.asusuigbo.frank.asusuigbo.connection.helpers

import android.support.v4.content.ContextCompat
import android.support.v4.widget.TextViewCompat
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.interfaces.ILesson
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.SentenceCreatorActivity
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.database.*

class DataLoader {
    companion object {
        fun populateList(lessonActivity: ILesson){
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val dbReference: DatabaseReference = database.getReference("Lessons/${lessonActivity.requestedLesson}")

            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.children){
                        val optionsList = ArrayList<String>()
                        val correctAnswer = d.child("CorrectAnswer").value.toString()
                        val question = d.child("Question").value.toString()

                        for(t in d.child("Options").children){
                            val text = t.value.toString()
                            optionsList.add(text)
                        }
                        val temp = QuestionGroup(question, optionsList, correctAnswer)
                        lessonActivity.dataList.add(temp)
                        if(lessonActivity is SentenceCreatorActivity) lessonActivity.workingList.add(temp)
                    }

                    if(lessonActivity is LessonActivity){
                        val que: TextView = lessonActivity.activity.findViewById(R.id.question_id)
                        que.text = lessonActivity.dataList[0].Question
                        buildRadioGroupContent(lessonActivity)
                    }else{
                        val que: TextView = lessonActivity.activity.findViewById(R.id.profile_question_id)
                        que.text = lessonActivity.dataList[0].Question
                        buildFlexBoxContent(lessonActivity)
                    }
                    lessonActivity.progressBar!!.visibility = View.GONE
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //do nothing
                }
            })
        }

        fun buildRadioGroupContent(lessonActivity: ILesson){
            val rg = lessonActivity.activity.findViewById<RadioGroup>(R.id.radio_group_id)
            for((index,item) in lessonActivity.dataList[0].Options.withIndex()){
                val view = RadioButton(lessonActivity.activity.applicationContext)
                val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(10,10,10,10)
                view.layoutParams = params
                view.text = item
                view.buttonTintList = ContextCompat.
                        getColorStateList(lessonActivity.activity.applicationContext, R.color.colorGrey)
                TextViewCompat.setTextAppearance(view, R.style.FontForRadioButton)
                view.background = ContextCompat.
                        getDrawable(lessonActivity.activity.applicationContext, R.drawable.option_background)
                view.setPadding(25,25,25,25)
                view.isClickable = true
                view.tag = index
                rg.addView(view)
            }
        }

        fun buildFlexBoxContent(sentenceCreatorActivity: ILesson) {
            for((index, item: String) in sentenceCreatorActivity.workingList[0].Options.withIndex()){
                val view = TextView(sentenceCreatorActivity.activity.applicationContext)
                val params = FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(10,10,10,10)
                view.layoutParams = params
                view.text = item
                TextViewCompat.setTextAppearance(view, R.style.FontForTextView)
                view.background = ContextCompat.getDrawable(sentenceCreatorActivity.activity.applicationContext, R.drawable.word_background)
                view.setPadding(25,25,25,25)
                view.isClickable = true
                view.tag = index
                view.setOnClickListener(sentenceCreatorActivity.textViewClickListener)
                val sourceFlexBoxLayout: FlexboxLayout = sentenceCreatorActivity.activity.findViewById(R.id.flexbox_source_id)
                sourceFlexBoxLayout.addView(view)
            }
        }
    }
}