package com.asusuigbo.frank.asusuigbo.fragments.weeklynews


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.newsinfo.NewsInfoAdapter
import com.asusuigbo.frank.asusuigbo.adapters.newsinfo.NewsInfoClickListener
import com.asusuigbo.frank.asusuigbo.databinding.FragmentWeeklyNewsBinding
import com.asusuigbo.frank.asusuigbo.models.NewsInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeeklyNewsFragment : Fragment() {

    private lateinit var binding: FragmentWeeklyNewsBinding
    private val viewModel: WeeklyNewsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weekly_news, container, false)
        initializeRecyclerView()
        viewModel.showNoNewsText.observe(viewLifecycleOwner, {
            if(it == true){
                binding.noNewsTextView.visibility = View.VISIBLE
                viewModel.setShowNoNewsText(false)
                binding.progressBarWeeklyNews.visibility = View.GONE
            }
        })

        return binding.root
    }

    private fun initializeRecyclerView(){
        val recyclerViewAdapter = NewsInfoAdapter(NewsInfoClickListener {

        })
        val manager = LinearLayoutManager(requireContext())
        binding.weeklyNewsRecyclerview.apply {
            layoutManager = manager
            adapter = recyclerViewAdapter
        }
        viewModel.newsInfoList.observe(viewLifecycleOwner, {
            recyclerViewAdapter.submitList(it!!)
            binding.progressBarWeeklyNews.visibility = View.GONE
        })
    }

    private fun getList(): MutableList<NewsInfo>{
        val myList = mutableListOf<NewsInfo>()
        for(i in 0 until 5){
            val item = NewsInfo(
                "My headline", "ABC News", "",
                "https://firebasestorage.googleapis.com/v0/b/uploadimagestofirebase-ba178.appspot.com/o/Images%2F1603799540933.jpg?alt=media&token=05b69504-e986-4d5d-8ae9-bd51800fcc3e",
                "You are blessed by God almighty. You are blessed by God almighty. You are blessed by God almighty.",
                "", "20th November 2020"
            )
            myList.add(item)
        }
        return myList
    }

}// Required empty public constructor

// https://firebasestorage.googleapis.com/v0/b/uploadimagestofirebase-ba178.appspot.com/o/Images%2F1603799540933.jpg?alt=media&token=05b69504-e986-4d5d-8ae9-bd51800fcc3e