package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.R

class WeeklyNewsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weekly_news, container, false)

        return view
    }


}// Required empty public constructor
