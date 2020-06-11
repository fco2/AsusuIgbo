package com.asusuigbo.frank.asusuigbo.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.asusuigbo.frank.asusuigbo.MainActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.UserLesson
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpBtn: Button
    private lateinit var loginBtn: Button
    private lateinit var email: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var auth: FirebaseAuth

    private lateinit var progressBar: ProgressBar
    private var userLessonList: ArrayList<UserLesson> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpBtn = findViewById(R.id.sign_up_button_id)
        loginBtn = findViewById(R.id.login_link_id)
        email = findViewById(R.id.email_id)
        username = findViewById(R.id.username_id)
        password = findViewById(R.id.password_id)
        progressBar = findViewById(R.id.loading_sign_up_id)
        auth = FirebaseAuth.getInstance()

        populateUserLessons()
        signUpBtn.setOnClickListener(signUpClickListener)
        loginBtn.setOnClickListener(loginClickListener)
    }

    private val signUpClickListener = View.OnClickListener {
        val usernameText = username.text.toString()
        val emailText = email.text.toString()
        val passwordText = password.text.toString()

        if(usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty()){
            Toast.makeText(this, "Please fill email, username and password.", Toast.LENGTH_LONG).show()
        }else{
            progressBar.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this) { task ->
                        if(task.isSuccessful){
                            //set up basic user info and login user
                            setUpNewUserData(usernameText)
                            startActivity(Intent(this, MainActivity::class.java))
                            progressBar.visibility = View.GONE
                            finish()
                        }else{
                            Toast.makeText(this, "Unable to create account: ${task.exception!!.message}",
                                    Toast.LENGTH_LONG).show()
                            progressBar.visibility = View.GONE
                        }
                    }
        }
    }

    private val loginClickListener = View.OnClickListener {
        progressBar.visibility = View.VISIBLE
        startActivity(Intent(this, LoginActivity::class.java))
        progressBar.visibility = View.GONE
        finish()
    }

    private fun setUpNewUserData(username: String){
        val dbReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        dbReference.child("Users").child(auth.currentUser!!.uid).child("Username").setValue(username)
        dbReference.child("Users").child(auth.currentUser!!.uid).child("WordsLearned").setValue("0")
        this.setUpUserLessonInfo(dbReference)
    }

    private fun setUpUserLessonInfo(dbReference: DatabaseReference){
        for((index, item) in this.userLessonList.withIndex()){
            dbReference.child("Users/${auth.currentUser!!.uid}/Lessons/$index/LessonName").setValue(item.LessonName)
            dbReference.child("Users/${auth.currentUser!!.uid}/Lessons/$index/LessonImage").setValue(item.LessonImage)
            dbReference.child("Users/${auth.currentUser!!.uid}/Lessons/$index/Unlocked").setValue(item.Unlocked)
        }

    }

    private fun populateUserLessons() {
        userLessonList.add(UserLesson("Intro", "lesson_intro", "True"))
        userLessonList.add(UserLesson("Intro2", "lesson_intro_2"))
        userLessonList.add(UserLesson("Phrases", "lesson_phrases"))
        userLessonList.add(UserLesson("Greetings", "lesson_greetings"))
        userLessonList.add(UserLesson("Home", "lesson_home"))
        userLessonList.add(UserLesson("Family", "lesson_family"))
        userLessonList.add(UserLesson("Family 2", "lesson_family_2"))
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
}
