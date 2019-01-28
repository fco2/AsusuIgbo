package com.asusuigbo.frank.asusuigbo.fragments

import android.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asusuigbo.frank.asusuigbo.adapters.LessonInfoAdapter
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.LessonInfo

class LessonsFragment : Fragment() {

    var recyclerView: RecyclerView? = null
    var myList: ArrayList<LessonInfo> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_lessons, container, false)

        recyclerView = view?.findViewById(R.id.recycler_view_id)
        initializeList()

        if( recyclerView != null){
            val glm = GridLayoutManager(activity?.applicationContext, 2)
            recyclerView?.layoutManager = glm

            recyclerView!!.hasFixedSize()
            val rva = LessonInfoAdapter(myList)
            recyclerView!!.adapter = rva

            //set up divider for recycler view
            /*val dividerDecor = DividerItemDecoration(activity.applicationContext, DividerItemDecoration.VERTICAL)
            dividerDecor.setDrawable(ContextCompat.getDrawable(activity, R.drawable.shape_divider))
            recyclerView!!.addItemDecoration(dividerDecor) */

            //rounded corners
            recyclerView!!.clipToOutline = true
        }

        return view
    }

    private fun initializeList(){
        myList.add(LessonInfo(1, "First lesson"))
        myList.add(LessonInfo(2, "Second lesson"))
        myList.add(LessonInfo(3, "Third lesson"))
        myList.add(LessonInfo(4, "Fourth lesson"))
    }
}// Required empty public constructor
