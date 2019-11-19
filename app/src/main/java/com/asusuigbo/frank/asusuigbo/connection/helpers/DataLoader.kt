package com.asusuigbo.frank.asusuigbo.connection.helpers

import android.view.View
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
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
                    lessonActivity.dataList.shuffle()

                    when(lessonActivity.dataList[0].LessonFormat ) {
                        "SingleSelect" -> lessonActivity.navigateToFragment("SingleSelect")
                        "MultiSelect" -> lessonActivity.navigateToFragment("MultiSelect")
                        "ImageSelect" -> lessonActivity.navigateToFragment("ImageSelect")
                        "WrittenText" -> lessonActivity.navigateToFragment("WrittenText")
                        "WordPair" -> lessonActivity.navigateToFragment("WordPair")
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
    }
}