package com.asusuigbo.frank.asusuigbo.connection.helpers

import android.view.View
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DataLoader {
    companion object {
        fun populateList(lessonActivity: LessonActivity){
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val dbReference: DatabaseReference = database
                .getReference("Lessons/${lessonActivity.requestedLesson}")

            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val auth = FirebaseAuth.getInstance()
                    for (d in dataSnapshot.children){
                        val questionGroup = d.getValue(QuestionGroup::class.java)!!
                        lessonActivity.dataList.add(questionGroup)
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
                            lessonActivity.wordsLearned = Integer.parseInt(temp)
                            temp = p0.child("Users").child(auth.currentUser!!.uid)
                                    .child("LessonsCompleted").value.toString()
                            lessonActivity.lessonsCompleted = Integer.parseInt(temp)
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