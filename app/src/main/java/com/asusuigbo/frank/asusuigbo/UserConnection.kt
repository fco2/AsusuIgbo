package com.asusuigbo.frank.asusuigbo

import android.view.View
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.google.firebase.database.*

class UserConnection {
    companion object {
        fun populateList(dataList: ArrayList<QuestionGroup>, requestedLesson: String,
                         question: TextView, radioGroup: RadioGroup, progressBar: ProgressBar){

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val dbReference: DatabaseReference  = database.getReference("Lessons/$requestedLesson")
            val optionA: RadioButton = radioGroup.findViewById(R.id.option_a_id)
            val optionB: RadioButton = radioGroup.findViewById(R.id.option_b_id)
            val optionC: RadioButton = radioGroup.findViewById(R.id.option_c_id)
            val optionD: RadioButton = radioGroup.findViewById(R.id.option_d_id)

            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.children){
                        val temp = QuestionGroup()
                        val optionsList = ArrayList<String>()
                        temp.CorrectAnswer = d.child("CorrectAnswer").value.toString()
                        temp.Question = d.child("Question").value.toString()

                        for(t in d.child("Options").children){
                            val text = t.value.toString()
                            optionsList.add(text)
                        }
                        temp.Options = optionsList
                        dataList.add(temp)
                    }
                    question.text = dataList[0].Question
                    optionA.text = dataList[0].Options[0]
                    optionB.text = dataList[0].Options[1]
                    optionC.text = dataList[0].Options[2]
                    optionD.text = dataList[0].Options[3]

                    progressBar.visibility = View.GONE
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //do nothing
                }
            })
        }
    }
}