package com.asusuigbo.frank.asusuigbo.fragments

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import com.asusuigbo.frank.asusuigbo.R
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var button: Button
    private lateinit var button2: Button
    private lateinit var view1: RelativeLayout
    private lateinit var view2: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_profile, container, false)
        this.button = view.findViewById(R.id.check_answer_button_id)
        //this.button2 = view.findViewById(R.id.check_answer_button_id_2)
        view1 = view.findViewById(R.id.view_1)
        view2 = view.findViewById(R.id.view_2)

        button.setOnClickListener(clickListner)
        //button2.setOnClickListener(clickListener2)
        return view
    }

    private val clickListner = View.OnClickListener {

        view2.visibility = View.GONE
        view1.visibility = View.VISIBLE
        /*val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.testing_layout, root_layout, false)
        view1.removeAllViews()
        view1.addView(view)*/
    }

    private val clickListener2 = View.OnClickListener {
        view1.animate().alpha(0.0f)
        //view1.visibility = View.GONE
        //view2.animate().alpha(1.0f)
    }

    private fun golden(){
        val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.testing_layout, root_layout, false)
        view1.removeAllViews()
        view1.addView(view)
    }

    private fun maybeSilver(){
        //view2.animate().alpha(0.0f)
        //view2.visibility = View.GONE
        //view1.animate().alpha(1.0f)
    }


}// Required empty public constructor
