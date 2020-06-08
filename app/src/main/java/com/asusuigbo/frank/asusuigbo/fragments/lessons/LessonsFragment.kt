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
import com.asusuigbo.frank.asusuigbo.models.UserLesson
import com.google.android.material.snackbar.Snackbar

class LessonsFragment : Fragment() {
    private lateinit var binding: FragmentLessonsBinding
    private lateinit var viewModel: LessonsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lessons, container, false)
        viewModel = ViewModelProvider(this).get(LessonsViewModel::class.java)
        binding.lessonsViewModel = viewModel
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

        binding.lessonsViewModel!!.lessonsList.observe(viewLifecycleOwner, Observer{
            populateRecyclerView(it)
            binding.progressBar.visibility = View.GONE
        })
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun populateRecyclerView(it: List<UserLesson>) {
        val adapter = LessonsAdapter(LessonsClickListener { lessonName ->
            val intent = Intent(context!!.applicationContext, LessonActivity::class.java)
            intent.putExtra("LESSON_NAME", lessonName)
            // You need this if starting activity outside an activity context
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //TODO: Test before uncomment ....
            Snackbar.make(binding.root, "lessonName: $lessonName", Snackbar.LENGTH_LONG).show()
            //context!!.applicationContext.startActivity(intent)
        })
        binding.recyclerView.adapter = adapter
        adapter.submitList(it)
    }
}