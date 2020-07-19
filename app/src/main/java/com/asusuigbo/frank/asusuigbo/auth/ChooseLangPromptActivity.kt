package com.asusuigbo.frank.asusuigbo.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.asusuigbo.frank.asusuigbo.MainActivity
import com.asusuigbo.frank.asusuigbo.databinding.ActivityChooseLangPromptBinding
import com.google.firebase.auth.FirebaseAuth

class ChooseLangPromptActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseLangPromptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //check if is authenticated
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
        binding = ActivityChooseLangPromptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.selectLanguage.setOnClickListener {
            startActivity(Intent(this, ChooseLangActivity::class.java))
        }
        binding.loginBtn.setOnClickListener{
            binding.loadingSpinner.visibility = View.VISIBLE
            startActivity(Intent(this, LoginActivity::class.java))
            binding.loadingSpinner.visibility = View.GONE
            finish()
        }
    }
}
