package com.asusuigbo.frank.asusuigbo.currentlesson

import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.databinding.ActivityCurrentLessonBinding
import com.asusuigbo.frank.asusuigbo.fragments.*
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.roundToInt

class CurrentLessonActivity : AppCompatActivity() {
     var wordsLearned: Int = 0
     var dataListSize: Int = 0   //vm (ViewModel)
    //TODO: remove completely
    var dataList: ArrayList<QuestionGroup> = ArrayList()  //vm (ViewModel)
    private var requestedLesson: String = ""   //vm (ViewModel)

    var popUpWindow: PopupWindow? = null
    lateinit var currentQuestion: QuestionGroup   //TODO: might not need
    var buttonState: UserButton = UserButton.AnswerNotSelected
    var selectedAnswer = ""    //vm (ViewModel)
    private var lessonIndex = 0

    lateinit var currentLessonViewModel: CurrentLessonViewModel
    private lateinit var factory: CurrentLessonViewModelFactory
    lateinit var binding: ActivityCurrentLessonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentLessonBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.spinnerProgressBar.visibility = View.VISIBLE
        this.setLessonData()
        factory = CurrentLessonViewModelFactory(this.requestedLesson)
        currentLessonViewModel = ViewModelProvider(this, factory).get(CurrentLessonViewModel::class.java)

        currentLessonViewModel.listReady.observe(this, Observer { ready ->
            if(ready){
                when(currentLessonViewModel.questionList.value?.get(0)?.QuestionFormat) {
                    "SingleSelect" -> navigateToFragment("SingleSelect")
                    /*"MultiSelect" -> navigateToFragment("MultiSelect")
                    "ImageSelect" -> navigateToFragment("ImageSelect")
                    "WrittenText" -> navigateToFragment("WrittenText")
                    "WordPair" -> navigateToFragment("WordPair")*/
                    else -> navigateToFragment("SingleSelect")
                }
                binding.spinnerProgressBar.visibility = View.GONE
            }
        })
    }

    private fun setLessonData(){
        this.requestedLesson = intent.getStringExtra("LESSON_NAME")!!
        this.lessonIndex = intent.getIntExtra("LESSON_INDEX", 0)
    }

    fun navigateToFragment(fragmentName: String = ""){
        if(this.popUpWindow != null)
            this.popUpWindow!!.dismiss()
        val fragmentManager = supportFragmentManager
        val ft = fragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.question_slide_in, R.anim.question_slide_out)
        when(fragmentName){
            "SingleSelect" -> {
                val singleSelectFragment = SingleSelectFragment(this)
                ft.replace(R.id.frame_layout_id, singleSelectFragment)
            }
            "MultiSelect" -> {
                val sentenceBuilder = SentenceBuilderFragment(this)
                ft.replace(R.id.frame_layout_id, sentenceBuilder)
            }
            "ImageSelect" -> {
                val imgChoiceFragment = ImgChoiceFragment(this)
                ft.replace(R.id.frame_layout_id, imgChoiceFragment)
            }
            "WrittenText" -> {
                val writtenTextFragment = WrittenTextFragment(this)
                ft.replace(R.id.frame_layout_id, writtenTextFragment)
            }
            "WordPair" -> {
                val wordPairFragment = WordPairFragment(this)
                ft.replace(R.id.frame_layout_id, wordPairFragment)
            }
            else -> {
                this.finishQuiz()
                return
            }
        }
        ft.commit()
    }

    fun setProgressBarStatus(){
        val percent: Double = (this.dataListSize - currentLessonViewModel.questionList.value!!.size).toDouble() / this.dataListSize.toDouble() * 100
        var result = percent.roundToInt()
        result = if(result == 0) 5 else result
        binding.lessonProgressBar.progress = result
    }

    private fun finishQuiz(){
        this.popUpWindow!!.dismiss()
        updateCompletedLesson()
    }

    private fun updateCompletedLesson(){
        val auth = FirebaseAuth.getInstance()
        val dbReference: DatabaseReference  = FirebaseDatabase.getInstance().reference

        //TODO: change how this is saved..might need to get data before save.
        val wordsLearned = wordsLearned + 9
        dbReference.child("Users").child(auth.currentUser!!.uid)
                .child("WordsLearned").setValue(wordsLearned.toString())
        //TODO: check for out of bounds index
        dbReference.child("Users/${auth.currentUser!!.uid}/Lessons/${lessonIndex + 1}/Unlocked").setValue("True")
        launchCompletedLessonScreen()
    }

    private fun launchCompletedLessonScreen(){
        val fm= supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        val lf = LessonCompletedFragment()
        ft.add(android.R.id.content, lf)
        ft.commit()
    }
}
