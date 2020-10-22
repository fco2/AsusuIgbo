package com.asusuigbo.frank.asusuigbo.fragments.lessons

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.asusuigbo.frank.asusuigbo.adapters.lessons.LessonsAdapter
import com.asusuigbo.frank.asusuigbo.adapters.lessons.LessonsClickListener
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.databinding.FragmentAllLessonsBinding
import com.asusuigbo.frank.asusuigbo.models.UserLesson
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class AllLessonsFragment : Fragment() {
    private lateinit var binding: FragmentAllLessonsBinding
    private val viewModel: LessonsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAllLessonsBinding.inflate(layoutInflater)
        binding.lessonsViewModel = viewModel
        val glm = GridLayoutManager(requireContext().applicationContext, 2)
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

        binding.lessonsViewModel!!.lessonsList.observe(viewLifecycleOwner, {
            populateRecyclerView(it)
            binding.progressBar.visibility = View.GONE
        })
        return binding.root
    }

    private fun populateRecyclerView(it: List<UserLesson>) {
        val adapter = LessonsAdapter(LessonsClickListener { lessonName, index ->
            val indexAndWordsLearned = index.toString() + "|" + viewModel.wordsLearned
            val action = AllLessonsFragmentDirections.actionAllLessonsFragmentToCurrentLessonActivity(lessonName,
                indexAndWordsLearned,viewModel.lessonsList.value!!.size, viewModel.activeLanguage.value!!)
            findNavController().navigate(action)
        })
        binding.recyclerView.adapter = adapter
        adapter.submitList(it)
    }

}

