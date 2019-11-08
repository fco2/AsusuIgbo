package com.asusuigbo.frank.asusuigbo.connection.helpers

import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.ImgChoiceOptionsAdapter
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DataLoader {
    companion object {
        fun populateList(lessonActivity: LessonActivity){
            //TODO: make a reference for dictionary here
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val dbReference: DatabaseReference = database
                .getReference("Lessons/${lessonActivity.requestedLesson}")

            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val auth = FirebaseAuth.getInstance()
                    //TODO: Load dictionary here 
                    for (d in dataSnapshot.children){
                        val optionsList = ArrayList<OptionInfo>()
                        val correctAnswer = d.child("CorrectAnswer").value.toString()
                        val question = d.child("Question").value.toString()
                        val lessonFormat = d.child("LessonFormat").value.toString()

                        for(t in d.child("Options").children){
                            val option = t.child("Option").value.toString()
                            val additionalInfo = t.child("AdditionalInfo").value.toString()
                            optionsList.add(OptionInfo(option, additionalInfo))
                        }
                        val temp = QuestionGroup(question, optionsList, correctAnswer, lessonFormat)
                        lessonActivity.dataList.add(temp)
                    }
                    lessonActivity.dataListSize = lessonActivity.dataList.size

                    when {
                        lessonActivity.dataList[0].LessonFormat == "SingleSelect" -> {
                            lessonActivity.buildSentenceView.multiSelectLayout.visibility = View.GONE
                            lessonActivity.writtenTextView.writtenTextLayout.visibility = View.GONE
                            lessonActivity.imgChoiceView.imgChoiceLayout.visibility = View.GONE
                            lessonActivity.singleSelectView.singleSelectLayout.visibility = View.VISIBLE
                            lessonActivity.singleSelectView.singleQuestionTextView.text =
                                lessonActivity.dataList[0].Question
                            buildRadioGroupContent(lessonActivity)
                        }
                        lessonActivity.dataList[0].LessonFormat == "MultiSelect" -> {
                            lessonActivity.singleSelectView.singleSelectLayout.visibility = View.GONE
                            lessonActivity.writtenTextView.writtenTextLayout.visibility = View.GONE
                            lessonActivity.imgChoiceView.imgChoiceLayout.visibility = View.GONE
                            lessonActivity.buildSentenceView.multiSelectLayout.visibility = View.VISIBLE
                            lessonActivity.buildSentenceView.multiQuestionTextView.text =
                                lessonActivity.dataList[0].Question
                            buildFlexBoxContent(lessonActivity)
                        }
                        lessonActivity.dataList[0].LessonFormat == "ImageSelect" -> {
                            lessonActivity.buildSentenceView.multiSelectLayout.visibility = View.GONE
                            lessonActivity.singleSelectView.singleSelectLayout.visibility = View.GONE
                            lessonActivity.writtenTextView.writtenTextLayout.visibility = View.GONE
                            lessonActivity.imgChoiceView.imgChoiceLayout.visibility = View.VISIBLE

                            lessonActivity.imgChoiceView.imgChoiceQuestion.text =
                                lessonActivity.dataList[0].Question
                            setUpImageChoiceView(lessonActivity)
                        }
                        else -> {
                            lessonActivity.buildSentenceView.multiSelectLayout.visibility = View.GONE
                            lessonActivity.singleSelectView.singleSelectLayout.visibility = View.GONE
                            lessonActivity.imgChoiceView.imgChoiceLayout.visibility = View.GONE
                            lessonActivity.writtenTextView.writtenTextLayout.visibility = View.VISIBLE
                            lessonActivity.writtenTextView.writtenTextQuestion.text =
                                lessonActivity.dataList[0].Question
                        }
                    }

                    database.reference.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(p0: DataSnapshot) {
                            var temp = p0.child("Users").child(auth.currentUser!!.uid)
                                    .child("WordsLearned").value.toString()
                            lessonActivity.lastUpdatedWL = Integer.parseInt(temp)
                            temp = p0.child("Users").child(auth.currentUser!!.uid)
                                    .child("LessonsCompleted").value.toString()
                            lessonActivity.lastUpdatedLC = Integer.parseInt(temp)
                            lessonActivity.progressBar!!.visibility = View.GONE
                        }
                        override fun onCancelled(p0: DatabaseError) { }
                    })
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

        fun buildRadioGroupContent(lessonActivity: LessonActivity){
            val rg = lessonActivity.activity.findViewById<RadioGroup>(R.id.radio_group_id)
            for((index,item) in lessonActivity.dataList[0].Options.withIndex()){
                val view = RadioButton(lessonActivity.activity.applicationContext)
                val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(10,10,10,10)
                view.layoutParams = params
                view.text = item.Option
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

        fun buildFlexBoxContent(lessonActivity: LessonActivity) {
            for((index, item: OptionInfo) in lessonActivity.dataList[0].Options.withIndex()){
                val view = TextView(lessonActivity.activity.applicationContext)
                val params = FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(10,10,10,10)
                view.layoutParams = params
                view.text = item.Option
                TextViewCompat.setTextAppearance(view, R.style.FontForTextView)
                view.background = ContextCompat.getDrawable(lessonActivity.activity.applicationContext, R.drawable.word_background)
                view.setPadding(25,25,25,25)
                view.isClickable = true
                view.tag = index
                view.setOnClickListener(lessonActivity.buildSentenceView.textViewClickListener)
                val sourceFlexBoxLayout: FlexboxLayout = lessonActivity.activity.findViewById(R.id.flexbox_source_id)
                sourceFlexBoxLayout.addView(view)
            }
        }

        fun setUpImageChoiceView(lessonActivity: LessonActivity){
            lessonActivity.imgChoiceView.recyclerView.layoutManager =
                GridLayoutManager(lessonActivity.applicationContext, 2)
            lessonActivity.imgChoiceView.recyclerView.hasFixedSize()
            //set this conditionally to prevent multiplication
            if(!lessonActivity.imgChoiceView.isItemDecoratorSet){
                lessonActivity.imgChoiceView.recyclerView.
                    addItemDecoration(lessonActivity.imgChoiceView.itemOffsetDecoration)
                lessonActivity.imgChoiceView.isItemDecoratorSet = true
            }
            val adapter = ImgChoiceOptionsAdapter(lessonActivity.dataList[0].Options, lessonActivity)
            lessonActivity.imgChoiceView.recyclerView.adapter = adapter
        }
    }
}