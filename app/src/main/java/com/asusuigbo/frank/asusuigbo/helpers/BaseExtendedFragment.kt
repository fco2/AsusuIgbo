package com.asusuigbo.frank.asusuigbo.helpers

import android.media.MediaPlayer
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.firebase.storage.FirebaseStorage

abstract class BaseExtendedFragment(private val lessonActivity: LessonActivity) : Fragment() {

    fun executeButtonAction() {
        when(lessonActivity.buttonState){
            UserButton.AnswerSelected -> {
                answerQuestion()
            }
            UserButton.NextQuestion -> {
                lessonActivity.navigateToFragment(lessonActivity.dataList[0].QuestionFormat)
            }
            else -> {
                lessonActivity.navigateToFragment()
            }
        }
    }

    fun answerQuestion(){
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

    fun setUpView(){
        this.updateOptions()
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
        lessonActivity.setProgressBarStatus()
    }

    fun playAudio(audioUrl: String){
        val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child(audioUrl).
            downloadUrl.addOnSuccessListener {
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(it.toString())
            mediaPlayer.setOnPreparedListener{player ->
                player.start()
            }
            mediaPlayer.prepareAsync()
        }
    }

    abstract fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int)

    abstract fun isCorrectAnswer(): Boolean

    abstract fun updateOptions()

    abstract fun disableOptions()
}