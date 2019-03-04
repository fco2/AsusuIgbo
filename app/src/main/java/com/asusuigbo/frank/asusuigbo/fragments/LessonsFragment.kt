package com.asusuigbo.frank.asusuigbo.fragments

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.connection.helpers.TableOfContentHelper
import com.asusuigbo.frank.asusuigbo.models.LessonInfo
import com.google.firebase.auth.FirebaseAuth

class LessonsFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    var dataList: ArrayList<LessonInfo> = ArrayList()
    var contextData: Context? = null
    lateinit var progressBar: ProgressBar
    lateinit var auth: FirebaseAuth // TODO: we could make this a property

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_lessons, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_id)
        this.progressBar = view.findViewById(R.id.progress_bar_id)
        TableOfContentHelper.populateList(this)

        auth = FirebaseAuth.getInstance()
        //Toast.makeText(activity.applicationContext, "USERNAME: ${auth.currentUser!!.uid}", Toast.LENGTH_LONG).show()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contextData = context
     }
}
