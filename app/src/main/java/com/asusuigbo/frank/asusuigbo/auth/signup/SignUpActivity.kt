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
        populateUserLessons()
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
                            this.setUpUserLessonInfo(dbReference)
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

    private fun setUpUserLessonInfo(dbReference: DatabaseReference){
        for((index, item) in this.userLessonList.withIndex()){
            dbReference.child("Users/${auth.currentUser!!.uid}/Language/${viewModel.language.value!!}/Lessons/$index/LessonName").setValue(item.LessonName)
            dbReference.child("Users/${auth.currentUser!!.uid}/Language/${viewModel.language.value!!}/Lessons/$index/LessonImage").setValue(item.LessonImage)
            dbReference.child("Users/${auth.currentUser!!.uid}/Language/${viewModel.language.value!!}/Lessons/$index/Unlocked").setValue(item.Unlocked)
        }
    }

    private fun populateUserLessons() {
        userLessonList.add(UserLesson("Lesson 1", "lesson_1", "True"))
        userLessonList.add(UserLesson("Lesson 2", "lesson_2"))
        userLessonList.add(UserLesson("Lesson 3", "lesson_3"))
        userLessonList.add(UserLesson("Lesson 4", "lesson_4"))
        userLessonList.add(UserLesson("Lesson 5", "lesson_5"))
        userLessonList.add(UserLesson("Lesson 6", "lesson_6"))
        userLessonList.add(UserLesson("Lesson 7", "lesson_7"))
        userLessonList.add(UserLesson("Lesson 8", "lesson_8"))
        userLessonList.add(UserLesson("Lesson 9", "lesson_9"))
        userLessonList.add(UserLesson("Lesson 10", "lesson_10"))
        userLessonList.add(UserLesson("Lesson 11", "lesson_11"))
        userLessonList.add(UserLesson("Lesson 12", "lesson_12"))
        userLessonList.add(UserLesson("Lesson 13", "lesson_13"))
        userLessonList.add(UserLesson("Lesson 14", "lesson_14"))
        userLessonList.add(UserLesson("Lesson 15", "lesson_15"))
        userLessonList.add(UserLesson("Lesson 16", "lesson_16"))
        userLessonList.add(UserLesson("Lesson 17", "lesson_17"))
        userLessonList.add(UserLesson("Lesson 18", "lesson_18"))
        userLessonList.add(UserLesson("Lesson 19", "lesson_19"))
        userLessonList.add(UserLesson("Lesson 20", "lesson_20"))
        userLessonList.add(UserLesson("Lesson 21", "lesson_21"))
        userLessonList.add(UserLesson("Lesson 22", "lesson_22"))
        userLessonList.add(UserLesson("Lesson 23", "lesson_23"))
        userLessonList.add(UserLesson("Lesson 24", "lesson_24"))
        userLessonList.add(UserLesson("Lesson 25", "lesson_25"))
        userLessonList.add(UserLesson("Lesson 26", "lesson_26"))
        userLessonList.add(UserLesson("Lesson 27", "lesson_27"))
        userLessonList.add(UserLesson("Lesson 28", "lesson_28"))
        userLessonList.add(UserLesson("Lesson 29", "lesson_29"))
        userLessonList.add(UserLesson("Lesson 30", "lesson_30"))
        userLessonList.add(UserLesson("Lesson 31", "lesson_31"))
        userLessonList.add(UserLesson("Lesson 32", "lesson_32"))
    }
}
