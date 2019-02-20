package com.asusuigbo.frank.asusuigbo.connection.helpers

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.SentenceInfo
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.database.*

class SentenceBuilderHelper {
    companion object {
        fun populateList(dataList: ArrayList<SentenceInfo>, view: View, activity: Activity, textViewClickListener: View.OnClickListener){
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val dbReference: DatabaseReference = database.getReference("Lessons/People")

            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.children){
                        val wordBlocks = ArrayList<String>()
                        val fullSentence = d.child("FullSentence").value.toString()

                        for(t in d.child("WordBlocks").children){
                            val word = t.value.toString()
                            wordBlocks.add(word)
                        }
                        val si = SentenceInfo(fullSentence, wordBlocks)
                        dataList.add(si)
                    }
                    val sourceFlexBoxLayout = view.findViewById<FlexboxLayout>(R.id.flexbox_source_id)
                    val textView = view.findViewById<TextView>(R.id.profile_question_id)
                    textView.text = dataList[0].fullSentence
                    buildFlexBoxContent(dataList[0].wordBlocks, sourceFlexBoxLayout, activity, textViewClickListener)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //do nothing
                }
            })
        }

        private fun buildFlexBoxContent(wordBlocks: ArrayList<String>,sourceFlexBoxLayout: FlexboxLayout,
                                        activity: Activity, textViewClickListener: View.OnClickListener) {
            for((index, item: String) in wordBlocks.withIndex()){
                val view = TextView(activity.applicationContext)
                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(10,10,10,10)
                view.layoutParams = params
                view.text = item
                view.background = ContextCompat.getDrawable(activity.applicationContext, R.drawable.word_background)
                view.setPadding(25,25,25,25)
                view.isClickable = true
                view.tag = index
                //view.setTextColor(ContextCompat.getColor(activity.applicationContext, R.color.colorWhite))
                view.setOnClickListener(textViewClickListener)
                sourceFlexBoxLayout.addView(view)
            }
        }
    }
}