package com.asusuigbo.frank.asusuigbo.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.MainActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.databinding.FragmentLessonCompletedBinding

class LessonCompletedFragment : Fragment() {

    private lateinit var binding: FragmentLessonCompletedBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lesson_completed, container, false)

        binding.finishedQuizButtonId.setOnClickListener(customOnClickListener)
        return binding.root
    }

    private var customOnClickListener = View.OnClickListener {
        val intent = Intent(requireContext().applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

        LessonCompletedFragmentDirections.navigateToAllLessons()
    }
}
