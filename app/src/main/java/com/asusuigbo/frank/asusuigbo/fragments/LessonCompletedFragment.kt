package com.asusuigbo.frank.asusuigbo.fragments

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.asusuigbo.frank.asusuigbo.MainActivity
import com.asusuigbo.frank.asusuigbo.R

class LessonCompletedFragment : Fragment() {

    private var button: Button? = null
    private var contextData: Context? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lesson_completed, container, false)
        button = view?.findViewById(R.id.finished_quiz_button_id)

        button!!.setOnClickListener(customOnClickListener)
        return view
    }

    private var customOnClickListener = View.OnClickListener {
        val intent = Intent(contextData, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contextData = context
    }
}
