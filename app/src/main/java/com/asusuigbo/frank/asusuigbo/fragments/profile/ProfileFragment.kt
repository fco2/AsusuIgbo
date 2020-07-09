package com.asusuigbo.frank.asusuigbo.fragments.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusuigbo.frank.asusuigbo.AddQuestionActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.chooselang.ChooseTextAdapter
import com.asusuigbo.frank.asusuigbo.adapters.chooselang.ChooseTextClickListener
import com.asusuigbo.frank.asusuigbo.auth.LoginActivity
import com.asusuigbo.frank.asusuigbo.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var factory: ProfileViewModelFactory

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.progressBarProfileId.visibility = View.VISIBLE

        binding.layoutToolbar.toolbarText.text = getString(R.string.profile_text)
        binding.layoutToolbar.currentLanguage.visibility = View.GONE
        auth = FirebaseAuth.getInstance()
        val app = activity!!.application
        factory = ProfileViewModelFactory(app)
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        setUpRecyclerView()

        viewModel.activeLang.observe(viewLifecycleOwner, Observer{
            val langText = "Current Language: $it"
            val dateText = "Started: ${viewModel.dateStarted.value}"
            binding.language.text = langText
            binding.date.text = dateText
            binding.username.text = viewModel.username
            if(viewModel.username == "Chukafc" || viewModel.username == "Lex Luthor")
                binding.addQuestionId.visibility = View.VISIBLE
            binding.progressBarProfileId.visibility = View.GONE
        })
        binding.signOutId.setOnClickListener(signOutClickListener)
        binding.addQuestionId.setOnClickListener(addQuestionClickListener)
        return binding.root
    }

    private fun setUpRecyclerView() {
        val manager = LinearLayoutManager(context!!.applicationContext)
        val itemDecoration = DividerItemDecoration(context!!.applicationContext, manager.orientation)
        val profileAdapter = ChooseTextAdapter(ChooseTextClickListener{
            Snackbar.make(binding.root, "Clicked $it", Snackbar.LENGTH_SHORT).show()
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
        startActivity(Intent(activity!!.applicationContext, AddQuestionActivity::class.java))
    }

    private val signOutClickListener = View.OnClickListener {
       if(auth.currentUser != null){
           auth.signOut()
           val intent = Intent(activity!!.applicationContext, LoginActivity::class.java)
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
           startActivity(intent)
        }
        /*val dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("Lessons/Intro/0").removeValue()
        Toast.makeText(context!!.applicationContext, "Deleted!", Toast.LENGTH_SHORT).show()*/
    }
}
