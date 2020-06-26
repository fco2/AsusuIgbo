package com.asusuigbo.frank.asusuigbo.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.asusuigbo.frank.asusuigbo.MainActivity
import com.asusuigbo.frank.asusuigbo.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = getInstance()
        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener(loginClickListener)
        binding.signUpLink.setOnClickListener(signUpClickListener)
    }

    private val loginClickListener = View.OnClickListener {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill email and password.", Toast.LENGTH_LONG).show()
        }else{
            binding.progressBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            binding.progressBar.visibility = View.GONE
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                            Timber.i(auth.currentUser!!.uid)
                        }else{
                            Toast.makeText(this, "Please enter a registered email and password.",
                                    Toast.LENGTH_LONG).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    }
        }
    }

    private val signUpClickListener = View.OnClickListener {
        binding.progressBar.visibility = View.VISIBLE
        startActivity(Intent(this, ChooseLangPromptActivity::class.java))
        binding.progressBar.visibility = View.GONE
        finish()
    }
}
