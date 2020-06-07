package com.asusuigbo.frank.asusuigbo.fragments.lessons


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.lessons.LessonsAdapter
import com.asusuigbo.frank.asusuigbo.adapters.lessons.LessonsClickListener
import com.asusuigbo.frank.asusuigbo.databinding.FragmentLessonsBinding

class LessonsFragment : Fragment() {
    private lateinit var binding: FragmentLessonsBinding
    private lateinit var viewModel: LessonsViewModel
    private lateinit var factory: LessonsViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lessons, container, false)
        factory = LessonsViewModelFactory(context!!.applicationContext)
        viewModel = ViewModelProvider(this, factory).get(LessonsViewModel::class.java)
        binding.lessonsViewModel = viewModel
        binding.lifecycleOwner = this

        binding.lessonsViewModel!!.lessonsList.observe(viewLifecycleOwner, Observer{
            populateRecyclerView()
        })
        return binding.root
    }

    private fun populateRecyclerView() {
        val glm = GridLayoutManager(context!!.applicationContext, 2)
        //this block below makes the recyclerView staggered
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int { //Index begins at 1
                return if(position % 3 == 0 || position % 3 == 1)
                    1
                else
                    glm.spanCount
            }
        }
        binding.recyclerView.layoutManager = glm
        binding.recyclerView.hasFixedSize()
        binding.recyclerView.adapter =
            LessonsAdapter(LessonsClickListener {
                //what to do when clicked
                val intent = Intent(context!!.applicationContext, LessonActivity::class.java)
                //Make these part of a view model
                intent.putExtra("LESSON_NAME", this.lessonList[position].lessonKey)
                intent.putExtra("LESSON_COUNT", (position + 1))
                // You need this if starting activity outside an activity context
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.applicationContext.startActivity(intent)
            })
        binding.progressBar.visibility = View.GONE
    }
}
