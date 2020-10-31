package com.asusuigbo.frank.asusuigbo.fragments.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.ChooseTextClickListener
import com.asusuigbo.frank.asusuigbo.adapters.chooselang.ChooseTextAdapter
import com.asusuigbo.frank.asusuigbo.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.progressBarProfileId.visibility = View.VISIBLE

        auth = FirebaseAuth.getInstance()
        setUpRecyclerView()

        viewModel.activeLang.observe(viewLifecycleOwner, {
            val langText = "Current Language: $it"
            val dateText = "Started: ${viewModel.dateStarted.value}"
            binding.language.text = langText
            binding.date.text = dateText
            binding.username.text = viewModel.username
            if(viewModel.username == "Chukafc" || viewModel.username == "Lex Luthor" || viewModel.username == "peter" || viewModel.username == "james3d") {
                binding.addQuestionId.visibility = View.VISIBLE
                binding.addNewsId.visibility = View.VISIBLE
            }
            binding.progressBarProfileId.visibility = View.GONE
        })
        binding.signOutId.setOnClickListener(signOutClickListener)
        binding.addQuestionId.setOnClickListener(addQuestionClickListener)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.addNewsId.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addNewsInfoFragment)
        }
        return binding.root
    }

    private fun setUpRecyclerView() {
        val manager = LinearLayoutManager(requireContext().applicationContext)
        val itemDecoration = DividerItemDecoration(requireContext().applicationContext, manager.orientation)
        val profileAdapter = ChooseTextAdapter(ChooseTextClickListener{
            navigateToActivity(it)
        })
        binding.recyclerView.apply {
            layoutManager = manager
            hasFixedSize()
            adapter = profileAdapter
            addItemDecoration(itemDecoration)
            profileAdapter.submitList(viewModel.dataList.value!!)
        }
    }

    private val addQuestionClickListener = View.OnClickListener{
        findNavController().navigate(R.id.action_profileFragment_to_addQuestionFragment)
    }

    private val signOutClickListener = View.OnClickListener {
       if(auth.currentUser != null){
           auth.signOut()
           findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
        /*val dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("Lessons/Intro/0").removeValue()
        Toast.makeText(context!!.applicationContext, "Deleted!", Toast.LENGTH_SHORT).show()*/
    }

    private fun navigateToActivity(s: String){
        when(s){
            "Languages" ->  findNavController().navigate(R.id.action_profileFragment_to_myLanguagesFragment)
            else -> { }
        }
    }


}
