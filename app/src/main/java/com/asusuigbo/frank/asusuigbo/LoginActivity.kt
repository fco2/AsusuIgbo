package com.asusuigbo.frank.asusuigbo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var loginBtn: Button
    private lateinit var signUpBtn: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = getInstance()
        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        setContentView(R.layout.activity_login)

        loginBtn = findViewById(R.id.login_button_id)
        signUpBtn = findViewById(R.id.sign_up_link_id)
        emailEditText = findViewById(R.id.email_id)
        passwordEditText = findViewById(R.id.password_id)
        progressBar = findViewById(R.id.loading_login_id)
        loginBtn.setOnClickListener(loginClickListener)
        signUpBtn.setOnClickListener(signUpClickListener)

        auth = getInstance()
    }

    private val loginClickListener = View.OnClickListener {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill email and password.", Toast.LENGTH_LONG).show()
        }else{
            progressBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            progressBar.visibility = View.GONE
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                            Log.d("DEBUG", auth.currentUser!!.uid)
                        }else{
                            Toast.makeText(this, "Please enter a registered email and password.",
                                    Toast.LENGTH_LONG).show()
                            progressBar.visibility = View.GONE
                        }
                    }
        }
    }

    private val signUpClickListener = View.OnClickListener {
        progressBar.visibility = View.VISIBLE
        startActivity(Intent(this, SignUpActivity::class.java))
        progressBar.visibility = View.GONE
        finish()
    }

}
