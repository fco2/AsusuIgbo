package com.asusuigbo.frank.asusuigbo.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.ChooseTextClickListener
import com.asusuigbo.frank.asusuigbo.adapters.chooselang.ChooseTextAdapter
import com.asusuigbo.frank.asusuigbo.databinding.FragmentChooseLangBinding
import com.asusuigbo.frank.asusuigbo.models.DataInfo

class ChooseLangFragment : Fragment() {
    private lateinit var binding: FragmentChooseLangBinding
    private var langList = ArrayList<DataInfo>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentChooseLangBinding.inflate(layoutInflater)
        binding.toolbarMain.setNavigationIcon(R.mipmap.icon_arrow_back_white_18dp)
        binding.toolbarMain.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_chooseLangFragment_to_chooseLangPromptFragment)
        }
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView(){
        populateList()
        val manager = LinearLayoutManager(requireContext())
        val dividerItemOffsetDecoration = DividerItemDecoration(requireContext(), manager.orientation)
        val chooseTextAdapter = ChooseTextAdapter(ChooseTextClickListener {
            val action = ChooseLangFragmentDirections.actionChooseLangFragmentToSignUpFragment(it)
            findNavController().navigate(action)
        })
        binding.chooseLangRecyclerView.apply {
            layoutManager = manager
            hasFixedSize()
            adapter = chooseTextAdapter
            addItemDecoration(dividerItemOffsetDecoration)
            chooseTextAdapter.submitList(langList)
        }
    }

    private fun populateList(){
        langList.add(DataInfo("Igbo"))
        langList.add(DataInfo("Oza"))
        langList.add(DataInfo("Yoruba"))
    }
}
