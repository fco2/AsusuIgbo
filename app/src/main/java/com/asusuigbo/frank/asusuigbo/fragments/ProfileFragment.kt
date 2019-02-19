package com.asusuigbo.frank.asusuigbo.fragments


import android.app.Fragment
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.R
import com.google.android.flexbox.FlexboxLayout


class ProfileFragment : Fragment() {

    var sourceFlexBoxLayout: FlexboxLayout? = null
    var destFlexBoxLayout: FlexboxLayout? = null
    private var dataList: ArrayList<String> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_profile, container, false)
        sourceFlexBoxLayout = view.findViewById(R.id.flexbox_source_id)
        destFlexBoxLayout = view.findViewById(R.id.flexbox_destination_id)

        //TODO: Refactor to FireBase
        this.createList()
        this.buildFlexBoxContent()

        return view
    }

    private val buttonClickListener = View.OnClickListener { v ->
        //TODO: add logic to store info in return list.
        if(destFlexBoxLayout!!.indexOfChild(v) == -1){
            sourceFlexBoxLayout!!.removeView(v)
            destFlexBoxLayout!!.addView(v)
        }else{
            destFlexBoxLayout!!.removeView(v)
            sourceFlexBoxLayout!!.addView(v)
        }
    }

    private fun buildFlexBoxContent(){
        for(item: String in dataList){
            val view = TextView(activity.applicationContext)
            val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(10,10,10,10)
            view.layoutParams = params
            view.text = item
            view.background = ContextCompat.getDrawable(activity.applicationContext, R.drawable.word_background)
            view.setPadding(25,25,25,25)
            view.isClickable = true
            //view.setTextColor(ContextCompat.getColor(activity.applicationContext, R.color.colorWhite))
            view.setOnClickListener(buttonClickListener)
            sourceFlexBoxLayout!!.addView(view)
        }
    }

    private fun createList(){
        dataList.add("Linux")
        dataList.add("Manjaro")
        dataList.add("Unix")
        dataList.add("Windows 8.1")
        dataList.add("Windows 10")
        dataList.add("Android")
        dataList.add("Fuschia OS")
        dataList.add("Ubuntu")
        dataList.add("Mac OS")
    }

}// Required empty public constructor
