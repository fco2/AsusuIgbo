package com.asusuigbo.frank.asusuigbo.fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.LoginActivity
import com.asusuigbo.frank.asusuigbo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ProfileFragment : Fragment() {

    private lateinit var signOutBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var username: TextView
    private lateinit var lessonsCompleted: TextView
    private lateinit var wordsLearned: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_profile, container, false)
        signOutBtn = view.findViewById(R.id.sign_out_id)
        username = view.findViewById(R.id.username_id)
        lessonsCompleted = view.findViewById(R.id.lessons_completed_value)
        wordsLearned = view.findViewById(R.id.words_learned_value)
        progressBar = view.findViewById(R.id.progress_bar_profile_id)
        progressBar.visibility = View.VISIBLE

        auth = FirebaseAuth.getInstance()
        setUserName()
        signOutBtn.setOnClickListener(signOutClickListener)
        return view
    }

    private val signOutClickListener = View.OnClickListener {
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
                wordsLearned.text = p0.child("Users").child(auth.currentUser!!.uid)
                        .child("WordsLearned").value.toString()
                lessonsCompleted.text = p0.child("Users").child(auth.currentUser!!.uid)
                        .child("LessonsCompleted").value.toString()

                progressBar.visibility = View.GONE
            }
        })
    }
}// Required empty public constructor
