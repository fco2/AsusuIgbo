package com.asusuigbo.frank.asusuigbo


import android.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    var recyclerView: RecyclerView? = null
    var myList: ArrayList<DataInfo> = ArrayList()
    //var linearLayoutOverCard: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_home, container, false)

        recyclerView = view?.findViewById(R.id.recycler_view_id)
        initializeList()

        if( recyclerView != null){
            val lm = LinearLayoutManager(activity?.applicationContext)
            recyclerView?.layoutManager = lm

            recyclerView!!.hasFixedSize()
            val rva = DataInfoAdapter(myList)
            recyclerView!!.adapter = rva

            val dividerDecor = DividerItemDecoration(activity.applicationContext, DividerItemDecoration.VERTICAL)
            dividerDecor.setDrawable(ContextCompat.getDrawable(activity, R.drawable.shape_divider))
            recyclerView!!.addItemDecoration(dividerDecor)

            //rounded corners
            //linearLayoutOverCard = view?.findViewById(R.id.linear_layout_id)
            recyclerView!!.clipToOutline = true
        }

        return view
    }

    private fun initializeList(){
        myList.add(DataInfo(1, "First lesson"))
        myList.add(DataInfo(2, "Second lesson"))
        myList.add(DataInfo(3, "Third lesson"))
        myList.add(DataInfo(4, "Fourth lesson"))
    }
}// Required empty public constructor
