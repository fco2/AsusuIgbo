package com.asusuigbo.frank.asusuigbo.fragments


import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.imagechoiceoptions.ImgChoiceOptionsAdapter
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.databinding.FragmentImgChoiceBinding
import com.asusuigbo.frank.asusuigbo.helpers.BaseExtendedFragment
import com.asusuigbo.frank.asusuigbo.helpers.ItemOffsetDecoration
import com.asusuigbo.frank.asusuigbo.models.UserButton
import java.util.*
import androidx.lifecycle.Observer
import com.asusuigbo.frank.asusuigbo.adapters.imagechoiceoptions.ImgOptionClickListener
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ImgChoiceFragment(private val currentLessonActivity: CurrentLessonActivity) : BaseExtendedFragment(currentLessonActivity) {

    private lateinit var itemOffsetDecoration: ItemOffsetDecoration
    lateinit var binding: FragmentImgChoiceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_img_choice, container, false)
        itemOffsetDecoration = ItemOffsetDecoration(this.context!!.applicationContext, R.dimen.item_offset)
        binding.buttonId.setOnClickListener(buttonClickListener)
        updateOptions()
        return binding.root
    }

    private val buttonClickListener = View.OnClickListener {
        this.executeButtonAction()
    }

    override fun isCorrectAnswer(): Boolean{
        return currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.CorrectAnswer.toLowerCase(Locale.getDefault()).trim() ==
                currentLessonActivity.currentLessonViewModel.selectedAnswer.value!!.toLowerCase(Locale.getDefault()).trim()
    }

    override fun updateOptions() {
        currentLessonActivity.currentLessonViewModel.currentQuestion.observe(viewLifecycleOwner, Observer{ question ->
            binding.imgChoiceQuestionId.text = currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.QuestionInfo.Question
            this.setUpImageChoiceView(question.Options)
            currentLessonActivity.currentLessonViewModel.setSelectedAnswer("")
            this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
            currentLessonActivity.setProgressBarStatus()
        })
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

    private fun setUpImageChoiceView(options: MutableList<OptionInfo>) {
        binding.imgChoiceRecyclerViewId.layoutManager = GridLayoutManager(this.currentLessonActivity, 2)
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
            this.playAudio(audio)
        //remove color filter for all items
        for(i in 0 until this.binding.imgChoiceRecyclerViewId.childCount){
            val v: View = this.binding.imgChoiceRecyclerViewId.getChildAt(i)
            v.background.clearColorFilter()
            val image = v.findViewById<ImageView>(R.id.AdditionalInfo)
            image.clearColorFilter()
        }
        //add color filter for selected item, both RelativeLayout and imageView
        val overlayColor = ContextCompat.getColor(this.currentLessonActivity.applicationContext,
            R.color.selectedImgChoiceOption)
        val filter = PorterDuffColorFilter(overlayColor, PorterDuff.Mode.MULTIPLY)
        view.background.colorFilter = filter
        val imgChoiceImg = view.findViewById(R.id.AdditionalInfo) as ImageView
        imgChoiceImg.colorFilter = filter
        //enable button click and set buttonState
        this.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
        this.currentLessonActivity.currentLessonViewModel.setSelectedAnswer(option)
    }
}
