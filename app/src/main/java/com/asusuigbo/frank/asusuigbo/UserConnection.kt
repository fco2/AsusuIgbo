package com.asusuigbo.frank.asusuigbo

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.QuestionsInfoAdapter
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.SharedData
import com.google.firebase.database.*

class UserConnection {
    companion object {
        fun populateList(dataList: ArrayList<QuestionGroup>, context: Context,
                         question: TextView, activity: LessonActivity, recyclerView: RecyclerView){

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val dbReference: DatabaseReference  = database.getReference("Lessons/QuestionGroup")
            recyclerView.layoutManager = LinearLayoutManager(context)

            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.children){
                        var temp = QuestionGroup()
                        var optionsList = ArrayList<String>()

                        temp.CorrectAnswer = d.child("CorrectAnswer").value.toString()
                        temp.Question = d.child("Question").value.toString()
                        temp.SelectedAnswer = d.child("SelectedAnswer").value.toString()

                        for(t in d.child("Options").children){
                            var text = t.value.toString()
                            optionsList.add(text)
                        }
                        temp.Options = optionsList

                        dataList.add(temp)
                    }
                    //TODO: remove this block of code - check and test first.
                    question.text = dataList[0].Question

                    recyclerView.adapter = QuestionsInfoAdapter(dataList[0].Options,
                            activity)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //do nothing
                }
            })
        }
    }
}