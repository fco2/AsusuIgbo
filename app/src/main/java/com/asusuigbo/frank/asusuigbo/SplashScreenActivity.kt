package com.asusuigbo.frank.asusuigbo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.asusuigbo.frank.asusuigbo.auth.LoginActivity
import com.asusuigbo.frank.asusuigbo.auth.chooselangprompt.ChooseLangPromptActivity
import com.asusuigbo.frank.asusuigbo.auth.chooselangprompt.ChooseLangViewModel
import com.asusuigbo.frank.asusuigbo.auth.chooselangprompt.ChooseLangViewModelFactory

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, ChooseLangPromptActivity::class.java ))
        finish() // this removes activity from back stack
    }
}
