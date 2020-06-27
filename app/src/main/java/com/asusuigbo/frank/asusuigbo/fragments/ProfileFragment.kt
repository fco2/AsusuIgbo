package com.asusuigbo.frank.asusuigbo.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.AddQuestionActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.auth.LoginActivity
import com.asusuigbo.frank.asusuigbo.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.progressBarProfileId.visibility = View.VISIBLE

        binding.layoutToolbar.toolbarText.text = getString(R.string.profile_text)
        binding.layoutToolbar.currentLanguage.visibility = View.GONE
        auth = FirebaseAuth.getInstance()
        setUserName()
        binding.signOutId.setOnClickListener(signOutClickListener)
        binding.addQuestionId.setOnClickListener(addQuestionClickListener)
        return binding.root
    }

    private val addQuestionClickListener = View.OnClickListener{
        startActivity(Intent(activity!!.applicationContext, AddQuestionActivity::class.java))
    }

    private val signOutClickListener = View.OnClickListener {
       if(auth.currentUser != null){
           auth.signOut()
           val intent = Intent(activity!!.applicationContext, LoginActivity::class.java)
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
           startActivity(intent)
        }
        /*val dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("Lessons/Intro/0").removeValue()
        Toast.makeText(context!!.applicationContext, "Deleted!", Toast.LENGTH_SHORT).show()*/
    }

    private fun setUserName(){
        val dbReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        dbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                binding.usernameId.text = p0.child("Users").child(auth.currentUser!!.uid).child("Username").value.toString()
                binding.wordsLearnedValue.text = p0.child("Users").child(auth.currentUser!!.uid)
                        .child("WordsLearned").value.toString()
                binding.lessonsCompletedValue.text = p0.child("Users").child(auth.currentUser!!.uid)
                        .child("LessonsCompleted").value.toString()

                binding.progressBarProfileId.visibility = View.GONE

                if(binding.usernameId.text == "Chukafc")
                    binding.addQuestionId.visibility = View.VISIBLE
            }
        })
    }
}
