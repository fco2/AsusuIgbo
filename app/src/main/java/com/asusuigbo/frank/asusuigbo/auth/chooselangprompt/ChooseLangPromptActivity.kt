package com.asusuigbo.frank.asusuigbo.auth.chooselangprompt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.asusuigbo.frank.asusuigbo.auth.ChooseLangActivity
import com.asusuigbo.frank.asusuigbo.auth.LoginActivity
import com.asusuigbo.frank.asusuigbo.databinding.ActivityChooseLangPromptBinding

class ChooseLangPromptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseLangPromptBinding
    private lateinit var viewModel: ChooseLangViewModel
    private lateinit var factory: ChooseLangViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLangPromptBinding.inflate(layoutInflater)
        //do everything before here
        factory = ChooseLangViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(ChooseLangViewModel::class.java)
        if(viewModel.activeLanguage.value != null){ //means user already has an account
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
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
