package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.databinding.FragmentWeeklyNewsBinding

class WeeklyNewsFragment : Fragment() {

    private lateinit var binding: FragmentWeeklyNewsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weekly_news, container, false)
        binding.layoutToolbar.toolbarText.text = getString(R.string.weekly_news)
        binding.layoutToolbar.currentLanguage.visibility = View.GONE
        return binding.root
    }


}// Required empty public constructor
