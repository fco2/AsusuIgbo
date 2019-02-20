package com.asusuigbo.frank.asusuigbo.fragments


import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.connection.helpers.SentenceBuilderHelper
import com.asusuigbo.frank.asusuigbo.models.SentenceInfo
import com.google.android.flexbox.FlexboxLayout


class ProfileFragment : Fragment() {

    var sourceFlexBoxLayout: FlexboxLayout? = null
    var destFlexBoxLayout: FlexboxLayout? = null
    //TODO: Refactor names to proper naming convention
    private var sentenceList: ArrayList<SentenceInfo> = ArrayList()
    private var selectedSentence: ArrayList<Int> = ArrayList()
    private var button: Button? = null
    private var textView: TextView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_profile, container, false)
        sourceFlexBoxLayout = view.findViewById(R.id.flexbox_source_id)
        destFlexBoxLayout = view.findViewById(R.id.flexbox_destination_id)
        button = view.findViewById(R.id.check_answer_button_id)
        textView = view.findViewById(R.id.profile_question_id)

        SentenceBuilderHelper.populateList(sentenceList, view, activity, textViewClickListener)
        button!!.setOnClickListener(buttonClickListener)

        return view
    }

    private val buttonClickListener = View.OnClickListener {
        var sentence = this.buildSentence()
        sentence += "."
        Toast.makeText(activity.applicationContext, sentence, Toast.LENGTH_LONG).show()
    }

    private val textViewClickListener = View.OnClickListener { v ->
        if(!button!!.isEnabled)
            button!!.isEnabled = true

        if(destFlexBoxLayout!!.indexOfChild(v) == -1){
            sourceFlexBoxLayout!!.removeView(v)
            destFlexBoxLayout!!.addView(v)
            selectedSentence.add(v.tag as Int)
        }else{
            destFlexBoxLayout!!.removeView(v)
            sourceFlexBoxLayout!!.addView(v)
            selectedSentence.remove(v.tag as Int)
        }
    }

    private fun buildSentence(): String{
        val sb = StringBuilder()
        for(item in selectedSentence){
            sb.append(sentenceList[0].wordBlocks.elementAt(item)).append(" ")
        }
        return sb.toString().trim()
    }

}// Required empty public constructor
