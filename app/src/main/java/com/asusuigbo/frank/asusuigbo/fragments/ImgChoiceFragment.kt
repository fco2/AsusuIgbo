package com.asusuigbo.frank.asusuigbo.fragments


import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.imagechoiceoptions.ImgChoiceOptionsAdapter
import com.asusuigbo.frank.asusuigbo.adapters.imagechoiceoptions.ImgOptionClickListener
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.databinding.FragmentImgChoiceBinding
import com.asusuigbo.frank.asusuigbo.helpers.ItemOffsetDecoration
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.asusuigbo.frank.asusuigbo.models.UserButton
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ImgChoiceFragment : Fragment() {

    private lateinit var itemOffsetDecoration: ItemOffsetDecoration
    lateinit var binding: FragmentImgChoiceBinding
    private lateinit var currentLesson: CurrentLessonActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentLesson = arguments!!["currentLesson"] as CurrentLessonActivity
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_img_choice, container, false)
        itemOffsetDecoration = ItemOffsetDecoration(this.context!!.applicationContext, R.dimen.item_offset)
        updateOptions()
        currentLesson.currentLessonViewModel.canAnswerQuestion.observe(viewLifecycleOwner, Observer{ canAnswer ->
            if(canAnswer){
                disableOptions()
                isCorrectAnswer()
                currentLesson.currentLessonViewModel.setHasCorrectBeenSet(true)
                currentLesson.currentLessonViewModel.setCanAnswerQuestion() //reset it back to false
            }
        })
        return binding.root
    }

    companion object{
        fun getInstance(currentLesson: CurrentLessonActivity) : ImgChoiceFragment{
            val fragment = ImgChoiceFragment()
            val bundle = Bundle()
            bundle.putSerializable("currentLesson", currentLesson)
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun isCorrectAnswer(){
        val result = currentLesson.currentLessonViewModel.currentQuestion.value!!.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                currentLesson.currentLessonViewModel.selectedAnswer.value!!.toLowerCase(Locale.getDefault()).trim()
        currentLesson.currentLessonViewModel.setIsCorrect(result)
    }

    private fun updateOptions() {
        //TODO: change logic on how it works -- just check how it works.
        currentLesson.currentLessonViewModel.currentQuestion.observe(viewLifecycleOwner, Observer{ question ->
            binding.imgChoiceQuestionId.text = currentLesson.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Question
            this.setUpImageChoiceView(question.Options)
            currentLesson.currentLessonViewModel.setSelectedAnswer("")
        })
    }

    private fun disableOptions(){
        val rvChildCount = binding.imgChoiceRecyclerViewId.childCount
        for(i in 0 until rvChildCount){
            val view: View = binding.imgChoiceRecyclerViewId.getChildAt(i)
            view.isClickable = false
        }
    }

    private fun setUpImageChoiceView(options: MutableList<OptionInfo>) {
        binding.imgChoiceRecyclerViewId.layoutManager = GridLayoutManager(this.currentLesson, 2)
        binding.imgChoiceRecyclerViewId.hasFixedSize()
        binding.imgChoiceRecyclerViewId.addItemDecoration(this.itemOffsetDecoration)


        val adapter = ImgChoiceOptionsAdapter(
            ImgOptionClickListener{ option, audio, view -> onClickImageAction(option, audio, view) }
        )
        binding.imgChoiceRecyclerViewId.adapter = null
        binding.imgChoiceRecyclerViewId.adapter = adapter
        adapter.submitList(options)
    }

    private fun onClickImageAction(option: String, audio: String, view: View){
        //play audio here
        if(audio != "")
            currentLesson.playAudio(audio)
        //remove color filter for all items
        for(i in 0 until this.binding.imgChoiceRecyclerViewId.childCount){
            val v: View = this.binding.imgChoiceRecyclerViewId.getChildAt(i)
            v.background.clearColorFilter()
            val image = v.findViewById<ImageView>(R.id.AdditionalInfo)
            image.clearColorFilter()
        }
        //add color filter for selected item, both RelativeLayout and imageView
        val overlayColor = ContextCompat.getColor(this.currentLesson.applicationContext,
            R.color.selectedImgChoiceOption)
        val filter = PorterDuffColorFilter(overlayColor, PorterDuff.Mode.MULTIPLY)
        view.background.colorFilter = filter
        val imgChoiceImg = view.findViewById(R.id.AdditionalInfo) as ImageView
        imgChoiceImg.colorFilter = filter

        //enable button click and set buttonState
        currentLesson.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
        this.currentLesson.currentLessonViewModel.setSelectedAnswer(option)
    }
}
