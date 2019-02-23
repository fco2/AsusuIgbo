package com.asusuigbo.frank.asusuigbo.connection.helpers

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v4.widget.TextViewCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.google.firebase.database.*

class UserConnection {
    companion object {
        fun populateList(dataList: ArrayList<QuestionGroup>, requestedLesson: String,
                         activity: Activity, progressBar: ProgressBar, listener: View.OnClickListener = View.OnClickListener{}){

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val dbReference: DatabaseReference  = database.getReference("Lessons/$requestedLesson")

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
                    val que: TextView = activity.findViewById(R.id.question_id)
                    que.text = dataList[0].Question
                    buildRadioGroupContent(dataList[0].Options, activity)

                    progressBar.visibility = View.GONE
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //do nothing
                }
            })
        }

        fun buildRadioGroupContent(optionsList: ArrayList<String>,activity: Activity){
            val rg = activity.findViewById<RadioGroup>(R.id.radio_group_id)
            for((index,item) in optionsList.withIndex()){
                val view = RadioButton(activity.applicationContext)
                val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(10,10,10,10)
                view.layoutParams = params
                view.text = item
                view.buttonTintList = ContextCompat.getColorStateList(activity.applicationContext, R.color.colorGrey)
                TextViewCompat.setTextAppearance(view, R.style.FontForRadioButton)
                view.background = ContextCompat.getDrawable(activity.applicationContext, R.drawable.option_background)
                view.setPadding(25,25,25,25)
                view.isClickable = true
                view.tag = index
                rg.addView(view)
            }
        }
    }
}