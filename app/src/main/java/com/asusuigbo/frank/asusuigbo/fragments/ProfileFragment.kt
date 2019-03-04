package com.asusuigbo.frank.asusuigbo.fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.LoginActivity
import com.asusuigbo.frank.asusuigbo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ProfileFragment : Fragment() {

    private lateinit var signOutBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var username: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_profile, container, false)
        signOutBtn = view.findViewById(R.id.sign_out_id)
        username = view.findViewById(R.id.username_id)
        auth = FirebaseAuth.getInstance()
        setUserName()
        signOutBtn.setOnClickListener(signOutClickListener)
        return view
    }

    private val signOutClickListener = View.OnClickListener {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val dbReference: DatabaseReference = database.reference
        //TODO: move from here --> update profile info
       // dbReference.child("Users").child(auth.currentUser!!.uid).child("Username").setValue("Frank")
        //dbReference.child("Users").child(auth.currentUser!!.uid).child("WordsLearned").setValue("0")

        //TODO: --> delete data from table.
        //dbReference.child("UserLessonsActivated").child(auth.currentUser!!.uid)
        //        .child("Person").removeValue()

        //TODO: this remains to sign user out
        if(auth.currentUser != null){
            auth.signOut()
            startActivity(Intent(activity.applicationContext, LoginActivity::class.java))
        }
    }

    private fun setUserName(){
        val dbReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        dbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                username.text = p0.child("Users").child(auth.currentUser!!.uid).child("Username").value.toString()
            }
        })
    }
}// Required empty public constructor
