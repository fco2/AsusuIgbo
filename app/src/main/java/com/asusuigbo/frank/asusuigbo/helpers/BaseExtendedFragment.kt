package com.asusuigbo.frank.asusuigbo.helpers

import android.media.MediaPlayer
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.currentlesson.CurrentLessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.firebase.storage.FirebaseStorage

abstract class BaseExtendedFragment(private val currentLessonActivity: CurrentLessonActivity) : Fragment() {

    fun executeButtonAction() {
        when(currentLessonActivity.buttonState){
            UserButton.AnswerSelected -> {
                answerQuestion()
            }
            UserButton.NextQuestion -> {
                currentLessonActivity.setCurrentQuestion()
                currentLessonActivity.navigateToFragment(currentLessonActivity.currentLessonViewModel.currentQuestion.value!!.QuestionFormat)
            }
            else -> {
                currentLessonActivity.navigateToFragment()
            }
        }
    }

    fun answerQuestion(){
        disableOptions()
        currentLessonActivity.popUpWindow = PopupHelper.displaySelectionInPopUp(currentLessonActivity, this.isCorrectAnswer())
        if(!this.isCorrectAnswer())
            currentLessonActivity.currentLessonViewModel.addQuestion(currentLessonActivity.currentLessonViewModel.currentQuestion.value!!)
        if(currentLessonActivity.currentLessonViewModel.questionList.value!!.size > 0)
            this.setUpButtonStateAndText(UserButton.NextQuestion, R.string.next_question_text)
        else
            this.setUpButtonStateAndText(UserButton.Finished, R.string.continue_text)
    }

    fun setUpView(){
        this.updateOptions()
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
        currentLessonActivity.setProgressBarStatus()
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