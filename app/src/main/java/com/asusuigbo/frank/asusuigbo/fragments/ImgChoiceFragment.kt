package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.ImgChoiceOptionsAdapter
import com.asusuigbo.frank.asusuigbo.helpers.BaseExtendedFragment
import com.asusuigbo.frank.asusuigbo.helpers.ItemOffsetDecoration
import com.asusuigbo.frank.asusuigbo.models.UserButton

/**
 * A simple [Fragment] subclass.
 */
class ImgChoiceFragment(val currentLessonActivity: CurrentLessonActivity) : BaseExtendedFragment(currentLessonActivity) {

    private lateinit var button: Button
    private var imgChoiceQuestion: TextView? = null
    lateinit var recyclerView: RecyclerView
    private lateinit var itemOffsetDecoration: ItemOffsetDecoration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_img_choice, container, false)
        button = view.findViewById(R.id.button_id)
        this.recyclerView = view.findViewById(R.id.img_choice_recycler_view_id)
        this.imgChoiceQuestion = view.findViewById(R.id.img_choice_question_id)
        itemOffsetDecoration = ItemOffsetDecoration(this.context!!.applicationContext, R.dimen.item_offset)
        //set question and view parameters here
        this.imgChoiceQuestion!!.text = currentLessonActivity.dataList[0].QuestionInfo.Question
        setUpView()
        button.setOnClickListener(buttonClickListener)
        return view
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    override fun isCorrectAnswer(): Boolean{
        return currentLessonActivity.selectedAnswer == currentLessonActivity.currentQuestion.CorrectAnswer
    }

    override fun updateOptions(){
        this.imgChoiceQuestion!!.text = currentLessonActivity.dataList[0].QuestionInfo.Question
        this.setUpImageChoiceView()
        currentLessonActivity.selectedAnswer = ""
    }

    override fun disableOptions(){
        val rvChildCount = this.recyclerView.childCount
        for(i in 0 until rvChildCount){
            val view: View = this.recyclerView.getChildAt(i)
            view.isClickable = false
        }
    }

    override fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button.text = getString(buttonText)
        currentLessonActivity.buttonState = buttonState
    }

    private fun setUpImageChoiceView(){
        this.recyclerView.layoutManager = GridLayoutManager(this.currentLessonActivity, 2)
        this.recyclerView.hasFixedSize()
        this.recyclerView.addItemDecoration(this.itemOffsetDecoration)
        this.currentLessonActivity.dataList[0].Options.shuffle()
        val adapter = ImgChoiceOptionsAdapter(this.currentLessonActivity.dataList[0].Options, this)
        this.recyclerView.adapter = null
        this.recyclerView.adapter = adapter
        this.recyclerView.adapter!!.notifyDataSetChanged()
    }
}
