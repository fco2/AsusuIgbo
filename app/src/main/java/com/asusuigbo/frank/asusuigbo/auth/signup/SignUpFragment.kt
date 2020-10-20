package com.asusuigbo.frank.asusuigbo.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.database.LanguageInfo
import com.asusuigbo.frank.asusuigbo.databinding.FragmentSignUpBinding
import com.asusuigbo.frank.asusuigbo.helpers.DateHelper
import com.asusuigbo.frank.asusuigbo.helpers.LessonsHelper
import com.asusuigbo.frank.asusuigbo.models.UserLesson
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var userLessonList: ArrayList<UserLesson> = ArrayList()
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        val lang = SignUpFragmentArgs.fromBundle(requireArguments()).selectedLanguage

        setLanguage(lang)
        userLessonList = LessonsHelper.createLessons()
        binding.signUpButton.setOnClickListener(signUpClickListener)
        binding.loginLink.setOnClickListener(loginClickListener)
        return binding.root
    }

    private fun setLanguage(lang: String) {
        viewModel.setLanguage(lang)
    }

    private val signUpClickListener = View.OnClickListener {
        val usernameText = binding.username.text.toString()
        val emailText = binding.email.text.toString()
        val passwordText = binding.password.text.toString()

        if(usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty()){
            Snackbar.make(binding.root, "Please fill email, username and password.", Snackbar.LENGTH_SHORT).show()
        }else{
            binding.signUpProgressBar.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if(task.isSuccessful){
                            //set up basic user info and login user
                            val dbReference: DatabaseReference = FirebaseDatabase.getInstance().reference
                            val languageInfo = LanguageInfo(auth.currentUser!!.uid, viewModel.language.value!!, true, DateHelper.getFormattedDate())

                            this.viewModel.insert(languageInfo)
                            setUpNewUserData(dbReference, usernameText)
                            LessonsHelper.saveLessonsToFirebase(auth, userLessonList, viewModel.language.value!!)
                            findNavController().navigate(R.id.action_signUpFragment_to_allLessonsFragment)
                            binding.signUpProgressBar.visibility = View.GONE
                            Snackbar.make(binding.root, "Account created successfully!", Snackbar.LENGTH_SHORT).show()
                        }else{
                            Snackbar.make(binding.root, "Unable to create account: ${task.exception!!.message}", Snackbar.LENGTH_SHORT).show()
                            binding.signUpProgressBar.visibility = View.GONE
                        }
                    }
        }
    }

    private val loginClickListener = View.OnClickListener {
        binding.signUpProgressBar.visibility = View.VISIBLE
         findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
    }

    private fun setUpNewUserData(dbReference: DatabaseReference, username: String){
        dbReference.child("Users").child(auth.currentUser!!.uid).child("Username").setValue(username)
        dbReference.child("Users").child(auth.currentUser!!.uid).child("WordsLearned").setValue("0")
    }
}
