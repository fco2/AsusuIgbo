package com.asusuigbo.frank.asusuigbo.fragments.lessons


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.LessonInfoAdapter
import com.asusuigbo.frank.asusuigbo.databinding.FragmentLessonsBinding
import timber.log.Timber

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

        binding.lessonsViewModel!!.viewableLessons.observe(viewLifecycleOwner, Observer{ lessonsViewable ->
            populateRecyclerView(lessonsViewable)
        })
        return binding.root
    }

    private fun populateRecyclerView(lessonsViewable: Int) {
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
        binding.recyclerView.adapter = LessonInfoAdapter(binding.lessonsViewModel!!.lessonsList.value!!,
            context!!.applicationContext, lessonsViewable)
        binding.progressBar.visibility = View.GONE
    }
}
