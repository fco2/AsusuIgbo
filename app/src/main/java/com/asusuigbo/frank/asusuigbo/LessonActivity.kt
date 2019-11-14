package com.asusuigbo.frank.asusuigbo

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.asusuigbo.frank.asusuigbo.connection.helpers.DataLoader
import com.asusuigbo.frank.asusuigbo.fragments.*
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.models.UserButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.max
import kotlin.math.roundToInt

class LessonActivity : AppCompatActivity() {
     var lastUpdatedWL: Int = 0
     var lastUpdatedLC: Int = 0
     var dataListSize: Int = 0
    var dataList: ArrayList<QuestionGroup> = ArrayList()
    var progressBar: ProgressBar? = null
    var requestedLesson: String = ""
    var activity: Activity = this
    var lessonsLayout: RelativeLayout? = null
    var popUpWindow: PopupWindow? = null
    lateinit var currentQuestion: QuestionGroup
    var buttonState: UserButton = UserButton.AnswerNotSelected
    private var lessonStatusProgressBar: ProgressBar? = null
    var selectedAnswer = ""
    private var lessonCount = 0
    lateinit var singleSelectFragment: SingleSelectFragment
    private lateinit var imgChoiceFragment: ImgChoiceFragment//ImgChoiceFragment.getInstance(this)
    private lateinit var writtenTextFragment: WrittenTextFragment
    private lateinit var sentenceBuilder: SentenceBuilderFragment
    var isItemDecoratorSet = false
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        this.progressBar = findViewById(R.id.progress_bar_lesson_id)
        this.lessonStatusProgressBar = findViewById(R.id.lesson_progress_id)
        this.lessonsLayout = findViewById(R.id.lessons_layout_id)
        this.imgChoiceFragment = ImgChoiceFragment(this)
        this.singleSelectFragment = SingleSelectFragment(this)
        this.writtenTextFragment = WrittenTextFragment(this)
        this.sentenceBuilder = SentenceBuilderFragment(this)
        this.progressBar!!.visibility = View.VISIBLE
        this.setLessonData()
        DataLoader.populateList(this)
    }

    private fun setLessonData(){
        this.requestedLesson = intent.getStringExtra("LESSON_NAME")
        this.lessonCount = intent.getIntExtra("LESSON_COUNT", 0)
    }

    fun navigateToFragment(fragmentName: String = ""){
        if(this.popUpWindow != null)
            this.popUpWindow!!.dismiss()
        val fragmentManager = supportFragmentManager
        val ft = fragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.question_slide_in, R.anim.question_slide_out)
        when(fragmentName){
            "SingleSelect" -> {
                ft.replace(R.id.frame_layout_id, singleSelectFragment)
            }
            "MultiSelect" -> {
                ft.replace(R.id.frame_layout_id, sentenceBuilder)
            }
            "ImageSelect" -> {
                ft.replace(R.id.frame_layout_id, imgChoiceFragment)
            }
            "WrittenText" -> {
                ft.replace(R.id.frame_layout_id, writtenTextFragment)
            }
            else -> {
                this.finishQuiz()
                return
            }
        }
        ft.commit()
    }

    fun setProgressBarStatus(){
        val percent: Double =
            (this.dataListSize - this.dataList.size).toDouble() / this.dataListSize.toDouble() * 100
        var result = percent.roundToInt()
        result = if(result == 0) 5 else result
        this.lessonStatusProgressBar!!.progress = result
    }

    private fun finishQuiz(){
        this.popUpWindow!!.dismiss()
        updateCompletedLesson()
    }

    private fun updateCompletedLesson(){
        val auth = FirebaseAuth.getInstance()
        val dbReference: DatabaseReference  = FirebaseDatabase.getInstance().reference

        val wordsLearned = max((lessonCount * 8), lastUpdatedWL)
        dbReference.child("Users").child(auth.currentUser!!.uid)
                .child("WordsLearned").setValue(wordsLearned.toString())
        val lessonsCompleted = max(lessonCount, lastUpdatedLC)
        dbReference.child("Users").child(auth.currentUser!!.uid)
                .child("LessonsCompleted").setValue(lessonsCompleted.toString())
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
