package com.asusuigbo.frank.asusuigbo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.asusuigbo.frank.asusuigbo.auth.ChooseLangPromptActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, ChooseLangPromptActivity::class.java ))
        finish() // this removes activity from back stack
    }
}
