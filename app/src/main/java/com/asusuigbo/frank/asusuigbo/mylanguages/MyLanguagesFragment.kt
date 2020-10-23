package com.asusuigbo.frank.asusuigbo.mylanguages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusuigbo.frank.asusuigbo.MainActivityViewModel
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.ChooseTextClickListener
import com.asusuigbo.frank.asusuigbo.adapters.mylanguages.MyLanguagesAdapter
import com.asusuigbo.frank.asusuigbo.databinding.FragmentMyLanguagesBinding
import com.asusuigbo.frank.asusuigbo.models.DataInfo
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyLanguagesFragment : Fragment() {

    private lateinit var binding: FragmentMyLanguagesBinding
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private var isItemDecorationSet = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMyLanguagesBinding.inflate(layoutInflater)
        activityViewModel.getAllLanguagesData().observe(requireActivity(), {
            activityViewModel.setListData(it)
        })
        activityViewModel.languagesList.observe(requireActivity(), {
            setUpRecyclerView(it)
        })

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                findNavController().navigate(R.id.action_myLanguagesFragment_to_profileFragment)
            }
        })
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun openAlertDialog(){
        var chosenLanguage: String
        val list = arrayOf("Igbo", "Oza", "Yoruba")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Languages")
        builder.setItems(list) { _, which ->
            chosenLanguage = when (which) {
                0 -> "Igbo"
                1 -> "Oza"
                2 -> "Yoruba"
                else -> "Igbo"
            }
            binding.progressBar.visibility = View.VISIBLE
            activityViewModel.setNewActiveLanguage(chosenLanguage)
            Snackbar.make(binding.root, "$chosenLanguage is now active!", Snackbar.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        }.create().show()
    }

    private fun setUpRecyclerView(it: List<DataInfo>) {
        val manager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), manager.orientation)
        val myLanguagesAdapter = MyLanguagesAdapter(ChooseTextClickListener {
            if(it == "+ Add Language") {
                openAlertDialog()
            }else{
                activityViewModel.setNewActiveLanguage(it)
            }
        })
        binding.languagesRecyclerView.apply {
            layoutManager = manager
            hasFixedSize()
            if(!isItemDecorationSet){
                addItemDecoration(itemDecoration)
                isItemDecorationSet = true
            }

            adapter = myLanguagesAdapter
            myLanguagesAdapter.submitList(it)
        }
    }
}
