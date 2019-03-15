package com.asusuigbo.frank.asusuigbo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
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
        dbReference.child("Users").child(auth.currentUser!!.uid).child("LessonsCompleted").setValue("0")
        dbReference.child("UserLessonsActivated").child(auth.currentUser!!.uid)
                .child("Intro").setValue("True")
    }
}
