package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.ImgChoiceOptionsAdapter
import com.asusuigbo.frank.asusuigbo.databinding.FragmentImgChoiceBinding
import com.asusuigbo.frank.asusuigbo.helpers.BaseExtendedFragment
import com.asusuigbo.frank.asusuigbo.helpers.ItemOffsetDecoration
import com.asusuigbo.frank.asusuigbo.models.UserButton
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ImgChoiceFragment(val currentLessonActivity: CurrentLessonActivity) : BaseExtendedFragment(currentLessonActivity) {

    private lateinit var itemOffsetDecoration: ItemOffsetDecoration

    lateinit var binding: FragmentImgChoiceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_img_choice, container, false)
        itemOffsetDecoration = ItemOffsetDecoration(this.context!!.applicationContext, R.dimen.item_offset)
        //set question and view parameters here
        binding.imgChoiceQuestionId.text = currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Question
        setUpView()
        binding.buttonId.setOnClickListener(buttonClickListener)
        return view
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    override fun isCorrectAnswer(): Boolean{
        return currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                currentLessonActivity.currentLessonViewModel.selectedAnswer.value!!.toLowerCase(Locale.getDefault()).trim()
    }

    override fun updateOptions(){
        binding.imgChoiceQuestionId.text = currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Question
        this.setUpImageChoiceView()
        currentLessonActivity.currentLessonViewModel.setSelectedAnswer("")
    }

    override fun disableOptions(){
        val rvChildCount = binding.imgChoiceRecyclerViewId.childCount
        for(i in 0 until rvChildCount){
            val view: View = binding.imgChoiceRecyclerViewId.getChildAt(i)
            view.isClickable = false
        }
    }

    override fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        binding.buttonId.isEnabled = buttonState != UserButton.AnswerNotSelected
        binding.buttonId.text = getString(buttonText)
        currentLessonActivity.buttonState = buttonState
    }

    private fun setUpImageChoiceView(){
        binding.imgChoiceRecyclerViewId.layoutManager = GridLayoutManager(this.currentLessonActivity, 2)
        binding.imgChoiceRecyclerViewId.hasFixedSize()
        binding.imgChoiceRecyclerViewId.addItemDecoration(this.itemOffsetDecoration)
        currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.Options.shuffle()
        val adapter = ImgChoiceOptionsAdapter(currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.Options, this)
        binding.imgChoiceRecyclerViewId.adapter = null
        binding.imgChoiceRecyclerViewId.adapter = adapter
        binding.imgChoiceRecyclerViewId.adapter!!.notifyDataSetChanged()
    }
}
