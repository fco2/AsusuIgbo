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
import com.asusuigbo.frank.asusuigbo.fragments.ImgChoiceFragment
import com.asusuigbo.frank.asusuigbo.fragments.SentenceBuilderFragment
import com.asusuigbo.frank.asusuigbo.fragments.SingleSelectFragment
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

                    //TODO: will refactor all these
                    when {
                        lessonActivity.dataList[0].LessonFormat == "SingleSelect" -> {
                            //TODO: refactor
                            lessonActivity.singleSelectFragment.singleQuestionTextView.text =
                                lessonActivity.dataList[0].Question
                            buildRadioGroupContent(lessonActivity.singleSelectFragment)
                        }
                        lessonActivity.dataList[0].LessonFormat == "MultiSelect" -> {
                            //lessonActivity.viewDisplayManager("MultiSelect")
                            lessonActivity.sentenceBuilder.multiQuestionTextView.text =
                                lessonActivity.dataList[0].Question
                            buildFlexBoxContent(lessonActivity.sentenceBuilder)
                        }
                        lessonActivity.dataList[0].LessonFormat == "ImageSelect" -> {
                            //lessonActivity.viewDisplayManager("ImageSelect")
                            lessonActivity.imgChoiceFragment.imgChoiceQuestion.text =
                                lessonActivity.dataList[0].Question
                            setUpImageChoiceView(lessonActivity.imgChoiceFragment)
                        }
                        lessonActivity.dataList[0].LessonFormat == "WrittenText" -> {
                            //lessonActivity.viewDisplayManager("WrittenText")
                            lessonActivity.writtenTextFragment.writtenTextQuestion.text =
                                lessonActivity.dataList[0].Question
                        }
                        else -> {
                            return
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

        fun buildRadioGroupContent(fragment: SingleSelectFragment){
            val rg = fragment.lessonActivity.findViewById<RadioGroup>(R.id.radio_group_id)
            rg.clearCheck()
            for((index,item) in fragment.lessonActivity.dataList[0].Options.withIndex()){
                val view = RadioButton(fragment.lessonActivity.applicationContext)
                val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(10,10,10,10)
                view.layoutParams = params
                view.text = item.Option
                view.buttonTintList = ContextCompat.
                        getColorStateList(fragment.lessonActivity.applicationContext, R.color.colorGrey)
                TextViewCompat.setTextAppearance(view, R.style.FontForRadioButton)
                view.background = ContextCompat.
                        getDrawable(fragment.lessonActivity.applicationContext, R.drawable.option_background)
                view.setPadding(25,25,25,25)
                view.isClickable = true
                view.tag = index
                rg.addView(view)
            }
        }

        fun buildFlexBoxContent(fragment: SentenceBuilderFragment) {
            for((index, item: OptionInfo) in fragment.lessonActivity.dataList[0].Options.withIndex()){
                val view = TextView(fragment.lessonActivity.applicationContext)
                val params = FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(10,10,10,10)
                view.layoutParams = params
                view.text = item.Option
                TextViewCompat.setTextAppearance(view, R.style.FontForTextView)
                view.background = ContextCompat.getDrawable(fragment.lessonActivity.applicationContext,
                    R.drawable.word_background)
                view.setPadding(25,25,25,25)
                view.isClickable = true
                view.tag = index
                view.setOnClickListener(fragment.textViewClickListener)
                val sourceFlexBoxLayout: FlexboxLayout =
                    fragment.lessonActivity.findViewById(R.id.flexbox_source_id)
                sourceFlexBoxLayout.addView(view)
            }
        }

        fun setUpImageChoiceView(fragment: ImgChoiceFragment){
            if (fragment.recyclerView.layoutManager == null){
                fragment.recyclerView.layoutManager =
                    GridLayoutManager(fragment.lessonActivity, 2)
                fragment.recyclerView.hasFixedSize()
            }

            //set this conditionally to prevent multiplication
            if(!fragment.isItemDecoratorSet){
                fragment.recyclerView.addItemDecoration(fragment.itemOffsetDecoration)
                fragment.isItemDecoratorSet = true
            }
            val adapter = ImgChoiceOptionsAdapter(fragment.lessonActivity.dataList[0].Options, fragment)
            fragment.recyclerView.adapter = adapter
        }
    }
}