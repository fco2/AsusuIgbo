package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
//import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.ImgChoiceOptionsAdapter
import com.asusuigbo.frank.asusuigbo.helpers.ItemOffsetDecoration
import com.asusuigbo.frank.asusuigbo.helpers.PopupHelper
import com.asusuigbo.frank.asusuigbo.models.UserButton
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ImgChoiceFragment(val lessonActivity: LessonActivity) : Fragment() {

    private lateinit var button: Button
    var imgChoiceQuestion: TextView? = null
    lateinit var recyclerView: RecyclerView
    lateinit var itemOffsetDecoration: ItemOffsetDecoration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_img_choice, container, false)
        button = view.findViewById(R.id.button_id)
        this.recyclerView = view.findViewById(R.id.img_choice_recycler_view_id)
        this.imgChoiceQuestion = view.findViewById(R.id.img_choice_question_id)
        itemOffsetDecoration = ItemOffsetDecoration(this.context!!.applicationContext, R.dimen.item_offset)
        setUpView()
        button.setOnClickListener(buttonClickListener)

        //set question and view parameters here
        this.imgChoiceQuestion!!.text = lessonActivity.dataList[0].Question
        this.setUpImageChoiceView()
        return view
    }

    companion object{
        fun getInstance(lessonActivity: LessonActivity): ImgChoiceFragment{
            return ImgChoiceFragment(lessonActivity)
        }
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    //TODO----- experimenting brute force approach -----------
    private fun executeButtonAction() {
        when(lessonActivity.buttonState){
            UserButton.AnswerSelected -> {
                answerQuestion()
            }
            UserButton.NextQuestion -> {
                //TODO: here, call lessonActivity to switch fragments
                //nextQuestion()
                lessonActivity.navigateToFragment(lessonActivity.dataList[0].LessonFormat)
            }
            else -> {
                //TODO: here call lesson activity to switch fragments to finish quiz
                //finishQuiz()
                lessonActivity.navigateToFragment()
            }
        }
    }

    private fun answerQuestion(){
        lessonActivity.currentQuestion = lessonActivity.dataList.removeAt(0)
        disableOptions()
        lessonActivity.popUpWindow =
            PopupHelper.displaySelectionInPopUp(lessonActivity, this.isCorrectAnswer())

        if(!this.isCorrectAnswer())
            lessonActivity.dataList.add(lessonActivity.currentQuestion)
        if(lessonActivity.dataList.size > 0)
            this.setUpButtonStateAndText(UserButton.NextQuestion, R.string.next_question_text)
        else
            this.setUpButtonStateAndText(UserButton.Finished, R.string.continue_text)
    }

    fun isCorrectAnswer(): Boolean{
        return lessonActivity.selectedAnswer == lessonActivity.currentQuestion.CorrectAnswer
    }

    private fun setUpView(){
        this.updateOptions()
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
        lessonActivity.setProgressBarStatus()
    }

    //TODO==== end brute force

    fun updateOptions(){
        lessonActivity.navigateToFragment("ImageSelect")
        this.imgChoiceQuestion!!.text = lessonActivity.dataList[0].Question
        this.setUpImageChoiceView()
        lessonActivity.selectedAnswer = ""
    }

    fun disableOptions(){
        val rvChildCount = this.recyclerView.childCount
        for(i in 0 until rvChildCount){
            val view: View = this.recyclerView.getChildAt(i)
            view.isClickable = false
        }
    }

    fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        this.button.isEnabled = buttonState != UserButton.AnswerNotSelected
        this.button.text = getString(buttonText)
        lessonActivity.buttonState = buttonState
    }

    private fun setUpImageChoiceView(){
        this.recyclerView.layoutManager = GridLayoutManager(this.lessonActivity, 2)
        this.recyclerView.hasFixedSize()
        this.recyclerView.addItemDecoration(this.itemOffsetDecoration)
        lessonActivity.isItemDecoratorSet = true
        val adapter = ImgChoiceOptionsAdapter(this.lessonActivity.dataList[0].Options, this)
        this.recyclerView.adapter = null
        this.recyclerView.adapter = adapter
        this.recyclerView.adapter!!.notifyDataSetChanged()
    }
}
