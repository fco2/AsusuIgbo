package com.asusuigbo.frank.asusuigbo.fragments

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.TableOfContentHelper
import com.asusuigbo.frank.asusuigbo.models.LessonInfo

class LessonsFragment : Fragment() {

    var recyclerView: RecyclerView? = null
    var myList: ArrayList<LessonInfo> = ArrayList()
    var contextData: Context? = null
    var progressBar: ProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_lessons, container, false)

        recyclerView = view?.findViewById(R.id.recycler_view_id)
        this.progressBar = view?.findViewById(R.id.progress_bar_id)
        TableOfContentHelper.populateList(myList, contextData, recyclerView, this.progressBar)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.contextData = context
    }
}
