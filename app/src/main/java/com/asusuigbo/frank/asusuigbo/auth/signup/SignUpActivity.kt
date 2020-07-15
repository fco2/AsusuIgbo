package com.asusuigbo.frank.asusuigbo.auth.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.asusuigbo.frank.asusuigbo.MainActivity
import com.asusuigbo.frank.asusuigbo.auth.LoginActivity
import com.asusuigbo.frank.asusuigbo.database.LanguageInfo
import com.asusuigbo.frank.asusuigbo.databinding.ActivitySignUpBinding
import com.asusuigbo.frank.asusuigbo.helpers.DateHelper
import com.asusuigbo.frank.asusuigbo.helpers.LessonsHelper
import com.asusuigbo.frank.asusuigbo.models.UserLesson
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var userLessonList: ArrayList<UserLesson> = ArrayList()
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel
    private lateinit var factory: SignUpViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val application = requireNotNull(this).application
        factory = SignUpViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(SignUpViewModel::class.java)
        setLanguage()
        userLessonList = LessonsHelper.createLessons()
        binding.signUpButton.setOnClickListener(signUpClickListener)
        binding.loginLink.setOnClickListener(loginClickListener)
    }

    private fun setLanguage(){
        val lang = intent.getStringExtra("LANGUAGE")
        viewModel.setLanguage(lang!!)
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
                    .addOnCompleteListener(this) { task ->
                        if(task.isSuccessful){
                            //set up basic user info and login user
                            val dbReference: DatabaseReference = FirebaseDatabase.getInstance().reference
                            val languageInfo = LanguageInfo(auth.currentUser!!.uid, viewModel.language.value!!, true, DateHelper.getFormattedDate())
                            this.viewModel.insert(auth.currentUser!!.uid, languageInfo)
                            setUpNewUserData(dbReference, usernameText)
                            LessonsHelper.saveLessonsToFirebase(auth, userLessonList, viewModel.language.value!!)
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
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
        startActivity(Intent(this, LoginActivity::class.java))
        binding.signUpProgressBar.visibility = View.GONE
        finish()
    }

    private fun setUpNewUserData(dbReference: DatabaseReference, username: String){
        dbReference.child("Users").child(auth.currentUser!!.uid).child("Username").setValue(username)
        dbReference.child("Users").child(auth.currentUser!!.uid).child("WordsLearned").setValue("0")
    }
}
