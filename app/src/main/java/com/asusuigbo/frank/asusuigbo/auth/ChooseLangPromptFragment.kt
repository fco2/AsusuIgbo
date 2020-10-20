package com.asusuigbo.frank.asusuigbo.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.databinding.FragmentChooseLangPromptBinding
import com.google.firebase.auth.FirebaseAuth

class ChooseLangPromptFragment : Fragment() {
    private lateinit var binding: FragmentChooseLangPromptBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //check if is authenticated
        val auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            findNavController().navigate(R.id.action_chooseLangPromptFragment_to_allLessonsFragment)
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_lang_prompt, container, false)
        binding.selectLanguage.setOnClickListener {
            findNavController().navigate(R.id.action_chooseLangPromptFragment_to_chooseLangFragment)
        }
        binding.loginBtn.setOnClickListener{
            binding.loadingSpinner.visibility = View.VISIBLE
            findNavController().navigate(R.id.action_chooseLangPromptFragment_to_loginFragment)
            binding.loadingSpinner.visibility = View.GONE
        }

        return binding.root
    }
}
