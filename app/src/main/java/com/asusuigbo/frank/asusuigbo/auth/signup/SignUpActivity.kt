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
import com.asusuigbo.frank.asusuigbo.models.UserLesson
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList

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
                            val languageInfo = LanguageInfo(auth.currentUser!!.uid, viewModel.language.value!!, true, getFormattedDate())
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
        userLessonList.add(UserLesson("Intro", "lesson_intro", "True"))
        userLessonList.add(UserLesson("Intro 2", "lesson_intro_2"))
        userLessonList.add(UserLesson("Phrases", "lesson_phrases"))
        userLessonList.add(UserLesson("Phrases 2", "lesson_phrases_2"))
        userLessonList.add(UserLesson("Greetings", "lesson_greetings"))
        userLessonList.add(UserLesson("Home", "lesson_home"))
        userLessonList.add(UserLesson("Family", "lesson_family"))
        userLessonList.add(UserLesson("Family 2", "lesson_family_2"))
        userLessonList.add(UserLesson("Anatomy", "lesson_anatomy"))
        userLessonList.add(UserLesson("Things", "lesson_things"))
        userLessonList.add(UserLesson("Things 2", "lesson_things_2"))
        userLessonList.add(UserLesson("Things 3", "lesson_things_3"))
        userLessonList.add(UserLesson("Places", "lesson_places"))
        userLessonList.add(UserLesson("Actions", "lesson_action"))
        userLessonList.add(UserLesson("Actions 2", "lesson_action_2"))
        userLessonList.add(UserLesson("Actions 3", "lesson_action_3"))
        userLessonList.add(UserLesson("School", "lesson_school"))
        userLessonList.add(UserLesson("Profession", "lesson_profession"))
        userLessonList.add(UserLesson("Work", "lesson_work"))
        userLessonList.add(UserLesson("Sports", "lesson_sports"))
        userLessonList.add(UserLesson("Food", "lesson_food"))
        userLessonList.add(UserLesson("Market", "lesson_market"))
        userLessonList.add(UserLesson("Activities", "lesson_activities"))
        userLessonList.add(UserLesson("Weather", "lesson_weather"))
        userLessonList.add(UserLesson("Questions", "lesson_question"))
        userLessonList.add(UserLesson("Questions 2", "lesson_question_2"))
        userLessonList.add(UserLesson("Directions", "lesson_directions"))
        userLessonList.add(UserLesson("Travel", "lesson_travel"))
        userLessonList.add(UserLesson("Vacation", "lesson_vacation"))
        userLessonList.add(UserLesson("Animals", "lesson_animals"))
        userLessonList.add(UserLesson("Sentences", "lesson_sentences"))
        userLessonList.add(UserLesson("Group Chat", "lesson_group_chat"))
    }

    private fun getFormattedDate(): String {
        val cal = Calendar.getInstance()
        val fmt = Formatter()
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH))
        val year = cal.get(Calendar.YEAR)
        val monthName = fmt.format("%tB", cal)
        return "$monthName, $year"
    }
}
