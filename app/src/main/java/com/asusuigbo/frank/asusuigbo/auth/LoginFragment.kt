package com.asusuigbo.frank.asusuigbo.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        auth = getInstance()
        if(auth.currentUser != null){
            findNavController().navigate(R.id.action_loginFragment_to_allLessonsFragment)
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.loginButton.setOnClickListener(loginClickListener)
        binding.signUpLink.setOnClickListener(signUpClickListener)

        return binding.root
    }

    private val loginClickListener = View.OnClickListener {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(requireContext(), "Please fill email and password.", Toast.LENGTH_LONG).show()
        }else{
            binding.progressBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            binding.progressBar.visibility = View.GONE
                            findNavController().navigate(R.id.action_loginFragment_to_allLessonsFragment)
                        }else{
                            Toast.makeText(requireContext(), "Please enter a registered email and password.",
                                    Toast.LENGTH_LONG).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    }
        }
    }

    private val signUpClickListener = View.OnClickListener {
        binding.progressBar.visibility = View.VISIBLE
        findNavController().navigate(R.id.action_loginFragment_to_chooseLangPromptFragment)
        binding.progressBar.visibility = View.GONE
    }
}
