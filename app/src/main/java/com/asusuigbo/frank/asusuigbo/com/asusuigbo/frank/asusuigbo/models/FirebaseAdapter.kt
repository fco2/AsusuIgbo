package com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener



class FirebaseAdapter {
    //private database: FirebaseDatabase = FirebaseD
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var dbReference = database.getReference("Lessons/QuestionGroup")
    var map: HashMap<String, QuestionGroup> = HashMap()
    var list: ArrayList<QuestionGroup> = ArrayList()

    init {
        //this.map = this.dbReference.child("QuestionGroup") //this is our map.
        this.populateList()
    }

    private fun populateList(){
        this.dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children){
                    val sampleModel = dataSnapshot.getValue<QuestionGroup>(QuestionGroup::class.java)
                    list.add(sampleModel!!)
                }

                Log.e("Data snapshot", "List size: ${list.size}.")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Data snapshot error", "" + databaseError)
            }
        })
    }
}