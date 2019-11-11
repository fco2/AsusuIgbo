package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.helpers.ItemOffsetDecoration

/**
 * A simple [Fragment] subclass.
 */
class ImgChoiceFragment(val lessonActivity: LessonActivity) : Fragment() {

    private lateinit var button: Button
    var imgChoiceQuestion: TextView = lessonActivity.activity.findViewById(R.id.img_choice_question_id)
    var recyclerView: RecyclerView = lessonActivity.activity.findViewById(R.id.img_choice_recycler_view_id)
    val itemOffsetDecoration = ItemOffsetDecoration(lessonActivity.applicationContext, R.dimen.item_offset)
    var isItemDecoratorSet = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_img_choice, container, false)
        button = view.findViewById(R.id.button_id)
        button.setOnClickListener(buttonClickListener)
        return view
    }

    companion object{
        fun getInstance(lessonActivity: LessonActivity): ImgChoiceFragment{
            return ImgChoiceFragment(lessonActivity)
        }
    }

    private val buttonClickListener = View.OnClickListener {
        //testDictActivity.toggleFragment("A")
    }

    fun isCorrectAnswer(): Boolean{
        return lessonActivity.selectedAnswer == lessonActivity.currentQuestion.CorrectAnswer
    }

    fun updateOptions(){
        //lessonActivity.viewDisplayManager("ImageSelect")
        this.imgChoiceQuestion.text = lessonActivity.dataList[0].Question
        DataLoader.setUpImageChoiceView(this)
        lessonActivity.selectedAnswer = ""
    }

    fun disableOptions(){
        val rvChildCount = this.recyclerView.childCount
        for(i in 0 until rvChildCount){
            val view: View = this.recyclerView.getChildAt(i)
            view.isClickable = false
        }
    }
}
