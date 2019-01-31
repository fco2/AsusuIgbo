package com.asusuigbo.frank.asusuigbo

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Visibility
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.adapters.QuestionGroupAdapter
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.google.firebase.database.*

class UserConnection {
    companion object {
        fun populateList(dataList: ArrayList<QuestionGroup>, requestedLesson: String,
                         question: TextView, activity: LessonActivity, recyclerView: RecyclerView){

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val dbReference: DatabaseReference  = database.getReference("Lessons/$requestedLesson")
            recyclerView.layoutManager = LinearLayoutManager(activity.applicationContext)

            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.children){
                        val temp = QuestionGroup()
                        val optionsList = ArrayList<String>()
                        temp.CorrectAnswer = d.child("CorrectAnswer").value.toString()
                        temp.Question = d.child("Question").value.toString()
                        temp.SelectedAnswer = d.child("SelectedAnswer").value.toString()

                        for(t in d.child("Options").children){
                            val text = t.value.toString()
                            optionsList.add(text)
                        }
                        temp.Options = optionsList
                        dataList.add(temp)
                    }
                    question.text = dataList[0].Question
                    recyclerView.adapter = QuestionGroupAdapter(dataList[0].Options,
                            activity)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //do nothing
                }
            })
        }
    }
}