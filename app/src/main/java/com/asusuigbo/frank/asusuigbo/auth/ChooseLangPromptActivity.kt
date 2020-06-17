package com.asusuigbo.frank.asusuigbo.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.databinding.ActivityChooseLangPromptBinding

class ChooseLangPromptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseLangPromptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLangPromptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.selectLanguage.setOnClickListener {
            startActivity(Intent(this, ChooseLangActivity::class.java))
            finish()
        }
    }
}
