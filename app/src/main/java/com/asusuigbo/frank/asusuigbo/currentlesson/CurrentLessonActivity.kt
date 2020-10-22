package com.asusuigbo.frank.asusuigbo.currentlesson

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.databinding.ActivityCurrentLessonBinding
import com.asusuigbo.frank.asusuigbo.fragments.*
import com.asusuigbo.frank.asusuigbo.helpers.PopupHelper
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class CurrentLessonActivity : AppCompatActivity() {
    private var wordsLearned: Int = 0
    private var dataListSize: Int = 0
    private var totalLessons: Int = 0

    private var popUpWindow: PopupWindow? = null
    private var buttonState: UserButton = UserButton.AnswerNotSelected
    private var lessonIndex = 0

    private val currentLessonViewModel: CurrentLessonViewModel by viewModels()
    lateinit var binding: ActivityCurrentLessonBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.spinnerProgressBar.visibility = View.VISIBLE
        this.setLessonData()

        currentLessonViewModel.listReady.observe(this, { ready ->
            if(ready){
                this.dataListSize = currentLessonViewModel.questionList.value!!.size
                this.currentLessonViewModel.setCurrentQuestion()
                when(currentLessonViewModel.currentQuestion.value!!.QuestionFormat) {
                    "SingleSelect" -> navigateToFragment("SingleSelect")
                    "MultiSelect" -> navigateToFragment("MultiSelect")
                    "ImageSelect" -> navigateToFragment("ImageSelect")
                    "WrittenText" -> navigateToFragment("WrittenText")
                    "WordPair" -> navigateToFragment("WordPair")
                    else -> navigateToFragment("SingleSelect")
                }
                binding.spinnerProgressBar.visibility = View.GONE
            }
        })
        binding.button.setOnClickListener(btnClickListener)
        currentLessonViewModel.hasCorrectBeenSet.observe(this, { wasCorrectSet ->
            if(wasCorrectSet){
                popUpWindow = PopupHelper.displaySelectionInPopUp(this, currentLessonViewModel)
                if(!currentLessonViewModel.isCorrect.value!!)
                    this.currentLessonViewModel.addQuestion(this.currentLessonViewModel.currentQuestion.value!!)
                if(this.currentLessonViewModel.questionList.value!!.size > 0)
                    this.setUpButtonStateAndText(UserButton.NextQuestion, R.string.next_question_text)
                else
                    this.setUpButtonStateAndText(UserButton.Finished, R.string.continue_text)
            }
        })
        currentLessonViewModel.playAudio.observe(this, {
            if(it != ""){
                playAudio(it)
                currentLessonViewModel.setPlayAudio("")
            }
        })
        currentLessonViewModel.resetBtnState.observe(this, {
            if(it){
                setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
                currentLessonViewModel.setResetBtnState(false)
            }
        })
    }

    private fun setLessonData(){
        val navArgs = CurrentLessonActivityArgs.fromBundle(intent.extras!!)
        val indexAndWordsLearned = navArgs.indexAndWordsLearned.split("|")//intent.getStringExtra("INDEX_AND_WORDS_LEARNED")!!.split("|")
        this.lessonIndex = indexAndWordsLearned[0].toInt()
        this.wordsLearned = indexAndWordsLearned[1].toInt()
        this.totalLessons = navArgs.numberOfLessons //intent.getIntExtra("NUM_OF_LESSONS", 0)
    }

    private fun navigateToFragment(fragmentName: String = ""){
        this.setUpButtonStateAndText(UserButton.AnswerNotSelected, R.string.answer_button_state)
        this.setProgressBarStatus()
        if(this::mediaPlayer.isInitialized)
            mediaPlayer.stop()

        if(this.popUpWindow != null)
            this.popUpWindow!!.dismiss()
        val fragmentManager = supportFragmentManager
        val ft = fragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.question_slide_in, R.anim.question_slide_out)
        when(fragmentName){
            "SingleSelect" -> {
                val singleSelectFragment = SingleSelectFragment()
                ft.replace(R.id.frame_layout_id, singleSelectFragment)
            }
            "MultiSelect" -> {
                val sentenceBuilder = SentenceBuilderFragment()
                ft.replace(R.id.frame_layout_id, sentenceBuilder)
            }
            "ImageSelect" -> {
                val imgChoiceFragment = ImgChoiceFragment()
                ft.replace(R.id.frame_layout_id, imgChoiceFragment)
            }
            "WrittenText" -> {
                val writtenTextFragment = WrittenTextFragment()
                ft.replace(R.id.frame_layout_id, writtenTextFragment)
            }
            "WordPair" -> {
                val wordPairFragment = WordPairFragment()
                ft.replace(R.id.frame_layout_id, wordPairFragment)
            }
            else -> {
                this.finishQuiz()
                return
            }
        }
        ft.commit()
    }

    private val btnClickListener = View.OnClickListener {
        when(buttonState){
            UserButton.AnswerSelected -> currentLessonViewModel.setCanAnswerQuestion()
            UserButton.NextQuestion -> {
                this.currentLessonViewModel.setCurrentQuestion()
                this.navigateToFragment(this.currentLessonViewModel.currentQuestion.value!!.QuestionFormat)
            }
            else -> this.navigateToFragment()
        }
    }

    private fun setUpButtonStateAndText(buttonState: UserButton, buttonText: Int){
        binding.button.isEnabled = buttonState != UserButton.AnswerNotSelected
        binding.button.text = getString(buttonText)
        this.buttonState = buttonState
    }

    private fun setProgressBarStatus(){
        val percent: Double = (this.dataListSize - currentLessonViewModel.questionList.value!!.size).toDouble() / this.dataListSize.toDouble() * 100
        var result = percent.roundToInt()
        result = if(result == 0) 5 else result
        binding.lessonProgressBar.progress = result
    }

    private fun playAudio(audioUrl: String){
        val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child(audioUrl).
        downloadUrl.addOnSuccessListener {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(it.toString())
            mediaPlayer.setOnPreparedListener{player ->
                player.start()
            }
            mediaPlayer.prepareAsync()
        }
    }

    private fun finishQuiz(){
        this.popUpWindow!!.dismiss()
        updateCompletedLesson()
    }

    private fun updateCompletedLesson(){
        val auth = FirebaseAuth.getInstance()
        val dbReference: DatabaseReference  = FirebaseDatabase.getInstance().reference
        val updatedWordsLearned = wordsLearned + dataListSize
        dbReference.child("Users/${auth.currentUser!!.uid}/WordsLearned").setValue(updatedWordsLearned.toString())

        if(lessonIndex < totalLessons)
            dbReference
                .child("Users/${auth.currentUser!!.uid}/Language/${currentLessonViewModel.activeLanguage.value}/Lessons/${lessonIndex + 1}/Unlocked")
                .setValue("True")
                launchCompletedLessonScreen()
    }

    private fun launchCompletedLessonScreen(){
        val fm= supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        val lf = LessonCompletedFragment()
        ft.add(android.R.id.content, lf)
        ft.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(popUpWindow != null)
            popUpWindow!!.dismiss()
        currentLessonViewModel.isCorrect.removeObservers(this)
        currentLessonViewModel.listReady.removeObservers(this)
        currentLessonViewModel.setHasCorrectBeenSet(false)
    }
}
